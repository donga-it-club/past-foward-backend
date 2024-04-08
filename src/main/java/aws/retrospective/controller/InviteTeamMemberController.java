package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.InviteTeamMemberDTO;
import aws.retrospective.service.InviteTeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InviteTeamMemberController {

    private final InviteTeamMemberService inviteTeamMemberService;

    @GetMapping("/teams/{teamId}/invitation-url")
    public CommonApiResponse<InviteTeamMemberDTO> getInvitation(@PathVariable Long teamId) { // 팀 ID에 해당하는 초대 정보를 가져오는 메서드
        // 팀 ID를 기반으로 초대 정보를 생성하고, 그 결과를 받아옴
        InviteTeamMemberDTO inviteTeamMemberDTO = inviteTeamMemberService.generateInvitation(teamId);

        // 성공적인 응답을 생성하여 반환함. 초대 정보 데이터와 함께 HTTP 상태 코드는 OK(200)으로 설정함.
        return CommonApiResponse.successResponse(HttpStatus.OK, inviteTeamMemberDTO);
    }

}
