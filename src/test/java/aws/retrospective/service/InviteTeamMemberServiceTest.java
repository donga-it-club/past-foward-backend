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
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InviteTeamMemberServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private InviteTeamMemberService inviteTeamMemberService;

    @Test
    public void testGenerateInvitation() {
        // 팀 정보를 가짜 객체로 설정
        Long teamId = 1L;
        Team team = mock(Team.class);
        when(team.getId()).thenReturn(teamId);

        // Mock 객체를 사용하여 팀 조회 결과 설정
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        // 테스트할 서비스 메서드 호출
        CommonApiResponse<InviteTeamMemberDTO> response = inviteTeamMemberService.generateInvitation(teamId);

        // 결과 검증
        assertEquals(HttpStatus.OK.value(), response.getCode(), "HTTP 상태 코드가 OK여야 합니다.");

        InviteTeamMemberDTO inviteTeamMemberDTO = response.getData();
        String invitationUrl = inviteTeamMemberDTO.getInvitationUrl();
        LocalDateTime expirationTime = inviteTeamMemberDTO.getExpirationTime();

        // 초대 링크 URL 검증
        assertEquals("예상된 초대 링크 URL", invitationUrl, "초대 링크 URL이 올바르지 않습니다.");

        // 초대 만료 시간 검증
        // 예상된 초대 만료 시간과 일치하는지 확인하는 코드 작성

        // teamRepository.findById() 메서드가 한 번 호출되었는지 확인
        verify(teamRepository, times(1)).findById(teamId);
    }
}
