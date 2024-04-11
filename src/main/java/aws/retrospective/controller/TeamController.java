package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.GetTeamUsersRequestDto;
import aws.retrospective.dto.GetTeamUsersResponseDto;
import aws.retrospective.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    // Action Items 눌렀을 때 팀에 속한 모든 회원 조회
    @Operation(summary = "팀에 속한 모든 회원 조회", description = "회고 보드를 진행 중인 팀에 속한 모든 회원을 조회하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200")
    })
    @GetMapping("/{teamId}/users")
    public CommonApiResponse<List<GetTeamUsersResponseDto>> getTeamMembers(@PathVariable Long teamId,
        @RequestBody @Valid GetTeamUsersRequestDto request) {
        List<GetTeamUsersResponseDto> response = teamService.getTeamMembers(teamId,
            request);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }
}
