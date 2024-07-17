package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.common.CurrentUser;
import aws.retrospective.dto.CreateRetrospectiveGroupDto;
import aws.retrospective.dto.CreateRetrospectiveGroupResponseDto;
import aws.retrospective.dto.GetRetrospectiveGroupResponseDto;
import aws.retrospective.dto.GetRetrospectiveGroupsDto;
import aws.retrospective.dto.PaginationResponseDto;
import aws.retrospective.dto.RetrospectiveGroupResponseDto;
import aws.retrospective.dto.UpdateRetrospectiveGroupBoardsDto;
import aws.retrospective.dto.UpdateRetrospectiveGroupDto;
import aws.retrospective.entity.User;
import aws.retrospective.service.RetrospectiveGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/retrospectiveGroups")
@Tag(name = "retrospectiveGroups")
@SecurityRequirement(name = "JWT")
public class RetrospectiveGroupController {

    private final RetrospectiveGroupService retrospectiveGroupService;

    @Operation(summary = "Create a new retrospective group", responses = {
        @ApiResponse(responseCode = "201", description = "Retrospective Group created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CommonApiResponse<CreateRetrospectiveGroupResponseDto> createRetrospectiveGroup(
        @CurrentUser User user, @Valid @RequestBody CreateRetrospectiveGroupDto createRetrospectiveGroupDto) {
        CreateRetrospectiveGroupResponseDto response = retrospectiveGroupService.createRetrospectiveGroup(user, createRetrospectiveGroupDto);

        return CommonApiResponse.successResponse(HttpStatus.CREATED, response);
    }

    @Operation(summary = "Update an existing retrospective group", responses = {
        @ApiResponse(responseCode = "200", description = "Retrospective Group updated successfully"),
        @ApiResponse(responseCode = "404", description = "Retrospective Group not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{retrospectiveGroupId}")
    public CommonApiResponse<RetrospectiveGroupResponseDto> updateRetrospectiveGroup(
        @CurrentUser User user, @PathVariable Long retrospectiveGroupId, @Valid @RequestBody UpdateRetrospectiveGroupDto dto) {
        RetrospectiveGroupResponseDto response = retrospectiveGroupService.updateRetrospectiveGroup(
            user, retrospectiveGroupId, dto);

        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @Operation(summary = "Update an existing retrospective group boards", responses = {
        @ApiResponse(responseCode = "200", description = "Retrospective Group updated successfully"),
        @ApiResponse(responseCode = "404", description = "Retrospective Group not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{retrospectiveGroupId}/boards")
    public CommonApiResponse<RetrospectiveGroupResponseDto> updateRetrospectiveGroupBoards(
    @CurrentUser User user, @PathVariable Long retrospectiveGroupId, @Valid @RequestBody UpdateRetrospectiveGroupBoardsDto dto) {
    RetrospectiveGroupResponseDto response = retrospectiveGroupService.updateRetrospectiveGroupBoards(
    user, retrospectiveGroupId, dto);

    return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @Operation(summary = "Read all retrospective groups", responses = {
        @ApiResponse(responseCode = "200", description = "Retrospective Groups read successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping()
    public CommonApiResponse<PaginationResponseDto<RetrospectiveGroupResponseDto>> getRetrospectiveGroups(
        @CurrentUser User user, @Valid GetRetrospectiveGroupsDto dto) {
        PaginationResponseDto<RetrospectiveGroupResponseDto> response = retrospectiveGroupService.getRetrospectiveGroups(user, dto);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @Operation(summary = "Read a retrospective group including retrospectives", responses = {
        @ApiResponse(responseCode = "200", description = "Retrospective Group read successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{retrospectiveGroupId}")
    public CommonApiResponse<GetRetrospectiveGroupResponseDto> getRetrospectiveGroup(
        @CurrentUser User user,
        @PathVariable Long retrospectiveGroupId) {
        GetRetrospectiveGroupResponseDto response = retrospectiveGroupService.getRetrospectiveGroup(
            user, retrospectiveGroupId);

        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @Operation(summary = "Delete a retrospective group", responses = {
        @ApiResponse(responseCode = "204", description = "Retrospective group deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Retrospective group not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{retrospectiveGroupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRetrospectiveGroup(
        @CurrentUser User user,
        @PathVariable("retrospectiveGroupId") Long retrospectiveGroupId) {
        retrospectiveGroupService.deleteRetrospectiveGroup(retrospectiveGroupId, user);

    }
}