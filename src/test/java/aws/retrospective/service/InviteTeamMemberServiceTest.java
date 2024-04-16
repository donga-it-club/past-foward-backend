package aws.retrospective.service;

import aws.retrospective.dto.InviteTeamMemberDTO;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.TeamInvite;
import aws.retrospective.repository.TeamInvitationRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InviteTeamMemberServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamInvitationRepository teamInvitationRepository;

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

        // 초대 링크 생성 시 사용될 값들
        String invitationCode = "testInvitationCode";
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(2);

        // 모의 데이터베이스에 저장되는 팀 초대 객체 생성
        TeamInvite savedTeamInvite = TeamInvite.builder()
                .team(team)
                .invitationCode(invitationCode)
                .expirationTime(expirationTime)
                .build();

        // Mock Repository에 저장된 TeamInvite 반환하도록 설정
        when(teamInvitationRepository.save(any(TeamInvite.class))).thenReturn(savedTeamInvite);

        // 서비스 메서드 호출
        InviteTeamMemberDTO inviteTeamMemberDTO = inviteTeamMemberService.generateInvitation(teamId);

        // 반환된 응답 검증
        assertNotNull(inviteTeamMemberDTO, "초대 정보가 null이면 안 됩니다.");
        assertNotNull(inviteTeamMemberDTO.getInvitationCode(), "초대 코드가 null이면 안 됩니다.");
        assertNotNull(inviteTeamMemberDTO.getInvitationUrl(), "초대 링크 URL이 null이면 안 됩니다.");
        assertNotNull(inviteTeamMemberDTO.getExpirationTime(), "초대 만료 시간이 null이면 안 됩니다.");

        // 팀 조회가 한 번 호출되었는지 확인
        verify(teamRepository, times(1)).findById(teamId);

        // QRCodeService의 generateQRCode 메서드가 적절한 매개변수로 호출되었는지 확인
        verify(qrCodeService, times(1)).generateQRCode(anyString(), any(LocalDateTime.class));
    }



    @Test
    void testValidateInvitation_ExpiredInvitation() {
        // 만료된 초대 코드 설정
        String invitationCode = "expiredInvitationCode";
        LocalDateTime expirationTime = LocalDateTime.now().minusHours(1); // 현재 시간보다 이전
        TeamInvite teamInvite = TeamInvite.builder()
                .invitationCode(invitationCode)
                .expirationTime(expirationTime)
                .build();

        // Mock Repository 설정
        when(teamInvitationRepository.findByInvitationCode(invitationCode)).thenReturn(Optional.of(teamInvite));

        // 서비스 메서드 호출 및 검증
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inviteTeamMemberService.validateInvitation(invitationCode),
                "만료된 초대 코드가 있으면 예외가 발생해야 합니다.");
        assertEquals("Invitation code has expired: " + invitationCode, exception.getMessage(),
                "예외 메시지가 올바르지 않습니다.");
    }

    @Test
    void testValidateInvitation_ValidInvitation() {
        // 유효한 초대 코드 설정
        String invitationCode = "validInvitationCode";
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(2);
        TeamInvite teamInvite = TeamInvite.builder()
                .invitationCode(invitationCode)
                .expirationTime(expirationTime)
                .build();

        // Mock Repository 설정
        when(teamInvitationRepository.findByInvitationCode(invitationCode)).thenReturn(Optional.of(teamInvite));

        // 서비스 메서드 호출 및 검증
        assertDoesNotThrow(() -> inviteTeamMemberService.validateInvitation(invitationCode),
                "유효한 초대 코드가 있으면 예외가 발생해서는 안 됩니다.");
    }

}




