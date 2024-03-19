package aws.retrospective.controller;


import aws.retrospective.dto.InviteTeamMemberDTO;
import aws.retrospective.service.InviteTeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InviteTeamMemberController {

    @Autowired
    private InviteTeamMemberService inviteTeamMemberService;

    @GetMapping("/teams/{teamId}/invitation")
    public ResponseEntity<InviteTeamMemberDTO> getInvitation(@PathVariable("teamId") String teamId) {
        // 팀 ID를 기반으로 초대 정보 생성
        InviteTeamMemberDTO inviteTeamMemberDTO = inviteTeamMemberService.generateInvitation(teamId);
        // 초대 링크 및 초대 QR 생성
        inviteTeamMemberService.generateInvitationQR(inviteTeamMemberDTO.getInvitationUrl());
        return ResponseEntity.ok(inviteTeamMemberDTO);
    }

}
