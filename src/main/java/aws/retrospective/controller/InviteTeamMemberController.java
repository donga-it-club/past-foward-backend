package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.InviteTeamMemberDTO;
import aws.retrospective.service.InviteTeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InviteTeamMemberController {

    private final InviteTeamMemberService inviteTeamMemberService;

    @GetMapping("/teams/{teamId}/invitation")
    public ResponseEntity<CommonApiResponse<InviteTeamMemberDTO>> getInvitation(@PathVariable Long teamId) {
        CommonApiResponse<InviteTeamMemberDTO> response = inviteTeamMemberService.generateInvitation(teamId);
        return ResponseEntity.ok(response);
    }
}
