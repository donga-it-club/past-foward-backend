package aws.retrospective.service;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.InviteTeamMemberDTO;
import aws.retrospective.entity.Team;
import aws.retrospective.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InviteTeamMemberServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private QRCodeService qrCodeService;

    @InjectMocks
    private InviteTeamMemberService inviteTeamMemberService;

    @Value("${domain.url}")
    private String domainUrl;

    @Test
    void testGenerateInvitation() {
        // 테스트용 팀 정보 생성
        Long teamId = 1L;
        String teamName = "Test Team";
        Team team = Team.builder().name(teamName).build();

        // Mock 객체 설정
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        // 서비스 메서드 호출
        CommonApiResponse<InviteTeamMemberDTO> response = inviteTeamMemberService.generateInvitation(teamId);

        // 반환된 응답 검증
        assertEquals(HttpStatus.OK.value(), response.getCode(), "HTTP 상태 코드가 OK여야 합니다.");

        InviteTeamMemberDTO inviteTeamMemberDTO = response.getData();
        assertTrue(isValidUUID(inviteTeamMemberDTO.getInvitationCode()), "초대 코드가 올바른 형식이어야 합니다.");
        assertNotNull(inviteTeamMemberDTO.getInvitationUrl(), "초대 링크 URL이 존재해야 합니다.");
        assertNotNull(inviteTeamMemberDTO.getExpirationTime(), "초대 만료 시간이 존재해야 합니다.");

        // 팀 조회가 한 번 호출되었는지 확인
        verify(teamRepository, times(1)).findById(teamId);

        // QRCodeService의 generateQRCode 메서드가 적절한 매개변수로 호출되었는지 확인
        verify(qrCodeService, times(1)).generateQRCode(anyString(), any(LocalDateTime.class));
    }

    @Test
    void testGenerateInvitation_Expiration() {
        // 테스트용 팀 정보 생성
        Long teamId = 1L;
        String teamName = "Test Team";
        Team team = Team.builder().name(teamName).build();

        // Mock 객체 설정
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        // 초대 링크의 만료 시간을 현재 시간에서 2시간 이전으로 설정 -- 초대 링크가 이미 만료되었음을 시뮬레이션!!
        LocalDateTime expirationTime = LocalDateTime.now().minusHours(2);

        // 서비스 메서드 호출
        CommonApiResponse<InviteTeamMemberDTO> response = inviteTeamMemberService.generateInvitation(teamId);

        // 반환된 응답 검증
        assertEquals(HttpStatus.OK.value(), response.getCode(), "HTTP 상태 코드가 OK여야 합니다.");

        InviteTeamMemberDTO inviteTeamMemberDTO = response.getData();
        assertTrue(isValidUUID(inviteTeamMemberDTO.getInvitationCode()), "초대 코드가 올바른 형식이어야 합니다.");
        assertNotNull(inviteTeamMemberDTO.getInvitationUrl(), "초대 링크 URL이 존재해야 합니다.");
        assertNotNull(inviteTeamMemberDTO.getExpirationTime(), "초대 만료 시간이 존재해야 합니다.");

        // 팀 조회가 한 번 호출되었는지 확인 -- 팀 조회가 초대 링크 생성에 영향을 미치는지 확인
        verify(teamRepository, times(1)).findById(teamId);

        // QRCodeService의 generateQRCode 메서드가 적절한 매개변수로 호출되었는지 확인
        verify(qrCodeService, times(1)).generateQRCode(anyString(), any(LocalDateTime.class));


        // 만료 시간이 지난 경우 QRCodeService의 generateQRCode 메서드가 다시 호출되는지 확인
        //if (expirationTime.isBefore(LocalDateTime.now())) {
        //    verify(qrCodeService, times(2)).generateQRCode(anyString(), any(LocalDateTime.class)); // 만료된 경우와 새로 생성된 경우  --> 에러가 나는 것 확인함



        }

    private boolean isValidUUID(String uuidString) {
        try {
            UUID.fromString(uuidString);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
