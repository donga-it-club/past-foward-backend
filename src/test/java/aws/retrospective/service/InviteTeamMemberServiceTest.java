package aws.retrospective.service;

import aws.retrospective.dto.InviteTeamMemberDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InviteTeamMemberServiceTest {
    private InviteTeamMemberService inviteTeamMemberService = new InviteTeamMemberService();

    @Test
    public void testGenerateInvitation() {
        String teamId = "exampleTeamId";

        InviteTeamMemberDTO inviteTeamMemberDTO = inviteTeamMemberService.generateInvitation(teamId);

        assertNotNull(inviteTeamMemberDTO);
        assertNotNull(inviteTeamMemberDTO.getInvitationCode());
        assertNotNull(inviteTeamMemberDTO.getInvitationUrl());
        assertNotNull(inviteTeamMemberDTO.getExpirationTime());
    }

    // 다른 메서드에 대한 테스트도 작성 가능
}
