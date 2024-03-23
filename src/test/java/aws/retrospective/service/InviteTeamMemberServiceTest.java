package aws.retrospective.service;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.InviteTeamMemberDTO;
import aws.retrospective.entity.Team;
import aws.retrospective.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InviteTeamMemberServiceTest {
    private InviteTeamMemberService inviteTeamMemberService = new InviteTeamMemberService();

    @Test
    public void testGenerateInvitation() {
        String teamId = "exampleTeamId";
        // Mock TeamRepository 생성
        TeamRepository teamRepository = mock(TeamRepository.class);
        // Mock Team 데이터 생성
        long teamId = 1L;

        InviteTeamMemberDTO inviteTeamMemberDTO = inviteTeamMemberService.generateInvitation(teamId);
        // 테스트에 사용할 팀 데이터 생성
        // 이 팀 데이터는 실제 팀이 존재하는 것으로 가정하고 테스트를 진행합니다.
        Team team = new Team("Example Team"); // Team 객체 생성
        team.setId(teamId); // 팀 아이디 설정

        assertNotNull(inviteTeamMemberDTO);
        assertNotNull(inviteTeamMemberDTO.getInvitationCode());
        assertNotNull(inviteTeamMemberDTO.getInvitationUrl());
        assertNotNull(inviteTeamMemberDTO.getExpirationTime());
    }
        when(teamRepository.findById(teamId)).thenReturn(java.util.Optional.of(team));

        // 테스트 대상 서비스 객체 생성
        InviteTeamMemberService inviteTeamMemberService = new InviteTeamMemberService(teamRepository);

    // 다른 메서드에 대한 테스트도 작성 가능
        // 테스트 실행 및 결과 확인
        CommonApiResponse<InviteTeamMemberDTO> response = inviteTeamMemberService.generateInvitation(teamId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertNotNull(response.getData());
        assertNotNull(response.getData().getInvitationCode());
        assertNotNull(response.getData().getInvitationUrl());
        assertNotNull(response.getData().getExpirationTime());
    }
}
