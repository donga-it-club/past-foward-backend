package aws.retrospective.service;

import aws.retrospective.dto.AcceptInvitationDto;
import aws.retrospective.dto.AcceptInviteResponseDto;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.TeamInvite;
import aws.retrospective.entity.User;
import aws.retrospective.repository.TeamInvitationRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.UserRepository;
import aws.retrospective.repository.UserTeamRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class InviteTeamMemberConcurrentTest {

    @Autowired
    private InviteTeamMemberService inviteTeamMemberService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamInvitationRepository teamInvitationRepository;

    @Autowired
    private UserTeamRepository userTeamRepository;

    @Autowired
    private UserRepository userRepository;

    private String invitationCode;
    private User testUser;
    private Team testTeam;

    @BeforeEach
    void setUp() {
        testTeam = teamRepository.save(Team.builder().name("동시성 테스트 팀").build());

        testUser = userRepository.save(
            User.builder()
                .email("concurrent@test.com")
                .username("concurrentUser")
                .tenantId(UUID.randomUUID().toString())
                .isAdministrator(false)
                .isEmailConsent(false)
                .build()
        );

        invitationCode = UUID.randomUUID().toString();
        TeamInvite invite = TeamInvite.builder()
            .team(testTeam)
            .invitationCode(invitationCode)
            .expirationTime(LocalDateTime.now().plusHours(1))
            .build();
        teamInvitationRepository.save(invite);
    }

    @Test
    @DisplayName("동시 100회 초대 수락 요청 시 UserTeam 레코드는 정확히 1건만 생성되어야 한다")
    void concurrentAcceptInvitation_shouldCreateExactlyOneUserTeam() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();
                    AcceptInvitationDto dto = new AcceptInvitationDto();
                    ReflectionTestUtils.setField(dto, "invitationCode", invitationCode);
                    AcceptInviteResponseDto response =
                        inviteTeamMemberService.acceptInvitation(dto, testUser);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        boolean completed = doneLatch.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(completed).isTrue()
            .withFailMessage("30초 내에 모든 스레드가 완료되지 않았습니다.");
        assertThat(errorCount.get()).isZero()
            .withFailMessage("예외 발생 횟수: %d. 모든 요청이 정상 응답을 반환해야 합니다.", errorCount.get());

        // 핵심 검증: DB에 UserTeam 레코드가 정확히 1건
        // findByTeamIdAndUserId로 직접 조회 (LAZY 로딩 없이 PK 조건만으로 존재 여부 확인)
        boolean exists = userTeamRepository
            .findByTeamIdAndUserId(testTeam.getId(), testUser.getId())
            .isPresent();
        assertThat(exists).isTrue()
            .withFailMessage("UserTeam 레코드가 존재하지 않습니다.");

        // 전체 count로 중복 삽입 여부 검증 (setUp에서 팀/유저 각 1건만 생성)
        long totalCount = userTeamRepository.count();
        assertThat(totalCount).isEqualTo(1)
            .withFailMessage("UserTeam 레코드가 %d건 생성되었습니다. 중복 가입 방지 실패.", totalCount);
    }
}
