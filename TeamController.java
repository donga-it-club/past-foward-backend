package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.common.CurrentUser;
import aws.retrospective.dto.AcceptInvitationDto;
import aws.retrospective.dto.GetTeamUsersResponseDto;
import aws.retrospective.dto.InviteTeamMemberDTO;
import aws.retrospective.entity.User;
import aws.retrospective.service.InviteTeamMemberService;
import aws.retrospective.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
@Tag(name = "teams")
@SecurityRequirement(name = "JWT")
public class TeamController {

    private final TeamService teamService;
    private final InviteTeamMemberService inviteTeamMemberService;

    // Action Items 눌렀을 때 팀에 속한 모든 회원 조회
    @Operation(summary = "팀에 속한 모든 회원 조회", description = "회고 보드를 진행 중인 팀에 속한 모든 회원을 조회하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping("/{teamId}/users")
    public CommonApiResponse<List<GetTeamUsersResponseDto>> getTeamMembers(
        @PathVariable Long teamId, @RequestParam Long retrospectiveId) {
        List<GetTeamUsersResponseDto> response = teamService.getTeamMembers(teamId,
            retrospectiveId);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @Operation(summary = "초대 링크를 통해 팀원 초대")
    @GetMapping("/{teamId}/invitation-url")
    public CommonApiResponse<InviteTeamMemberDTO> getInvitation(
        @PathVariable Long teamId) {
        InviteTeamMemberDTO inviteTeamMemberDTO = inviteTeamMemberService.generateInvitation(
            teamId);

        return CommonApiResponse.successResponse(HttpStatus.OK, inviteTeamMemberDTO);
    }

    @Operation(summary = "초대 링크를 통해 팀원 초대 수락")
    @ApiResponses(value = {@ApiResponse(responseCode = "204")})
    @PostMapping("accept-invitation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptInvitation(
        @CurrentUser User user,
        @Valid @RequestBody AcceptInvitationDto dto) {
        inviteTeamMemberService.acceptInvitation(dto, user);
    }

    @Operation(summary = "팀에서 회원 삭제")
    @ApiResponses(value = {@ApiResponse(responseCode = "204")})
    @DeleteMapping("/{teamId}/members/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTeamMember(
            @PathVariable @NotNull Long teamId,
            @PathVariable @NotNull Long userId) {
            inviteTeamMemberService.deleteUserFromTeam(teamId, userId);
    }
}
