package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.common.CurrentUser;
import aws.retrospective.dto.CreateRetrospectiveGroupDto;
import aws.retrospective.dto.CreateRetrospectiveGroupResponseDto;
import aws.retrospective.dto.GetRetrospectiveGroupsDto;
import aws.retrospective.dto.PaginationResponseDto;
import aws.retrospective.dto.RetrospectiveGroupResponseDto;
import aws.retrospective.dto.UpdateRetrospectiveGroupDto;
import aws.retrospective.entity.User;
import aws.retrospective.service.RetrospectiveGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
public class RetrospectiveGroupController {

    private final RetrospectiveGroupService retrospectiveGroupService;

    @Operation(summary = "회고 그룹 조회")
    @GetMapping()
    public CommonApiResponse<PaginationResponseDto<RetrospectiveGroupResponseDto>> getRetrospectiveGroups(
        @CurrentUser User user, @Valid GetRetrospectiveGroupsDto dto) {
        PaginationResponseDto<RetrospectiveGroupResponseDto> response = retrospectiveGroupService.getRetrospectiveGroups(
            user, dto);

        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @Operation(summary = "회고 그룹 수정")
    @PutMapping("/{retrospectiveGroupId}")
    public CommonApiResponse<RetrospectiveGroupResponseDto> updateRetrospectiveGroup(@CurrentUser User user,
        @PathVariable Long retrospectiveGroupId, @RequestBody @Valid UpdateRetrospectiveGroupDto dto) {
        RetrospectiveGroupResponseDto response = retrospectiveGroupService.updateRetrospectiveGroup(user,
            retrospectiveGroupId, dto);

        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @Operation(summary = "회고 그룹 생성")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CommonApiResponse<CreateRetrospectiveGroupResponseDto> createRetrospectiveGroup(
        @CurrentUser User user, @RequestBody @Valid CreateRetrospectiveGroupDto createRetrospectiveGroupDto) {
        CreateRetrospectiveGroupResponseDto response = retrospectiveGroupService.createRetrospectiveGroup(user,
            createRetrospectiveGroupDto);

        return CommonApiResponse.successResponse(HttpStatus.CREATED, response);

    }

    @Operation(summary = "회고 그룹 삭제")
    @DeleteMapping("/{retrospectiveGroupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRetrospectiveGroup(@CurrentUser User user, @PathVariable Long retrospectiveGroupId) {
        retrospectiveGroupService.deleteRetrospectiveGroup(retrospectiveGroupId, user);
    }

    @Operation(summary = "회고 그룹 북마크")
    @PatchMapping("/{retrospectiveGroupId}/bookmark")
    public CommonApiResponse<Boolean> toggleBookmark(@CurrentUser User user,
        @PathVariable Long retrospectiveGroupId) {
        boolean isBookmarked = retrospectiveGroupService.toggleBookmark(retrospectiveGroupId, user);

        return CommonApiResponse.successResponse(HttpStatus.OK, isBookmarked);
    }
}
