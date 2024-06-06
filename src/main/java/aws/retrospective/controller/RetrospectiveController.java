package aws.retrospective.controller;


import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.common.CurrentUser;
import aws.retrospective.dto.CreateRetrospectiveDto;
import aws.retrospective.dto.CreateRetrospectiveResponseDto;
import aws.retrospective.dto.GetRetrospectiveResponseDto;
import aws.retrospective.dto.GetRetrospectivesDto;
import aws.retrospective.dto.PaginationResponseDto;
import aws.retrospective.dto.RetrospectiveResponseDto;
import aws.retrospective.dto.UpdateRetrospectiveDto;
import aws.retrospective.entity.User;
import aws.retrospective.service.RetrospectiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/retrospectives")
@Tag(name = "retrospectives")
@SecurityRequirement(name = "JWT")
public class RetrospectiveController {

    private final RetrospectiveService retrospectiveService;


    @Operation(summary = "회고 조회")
    @GetMapping()
    public CommonApiResponse<PaginationResponseDto<RetrospectiveResponseDto>> getRetrospectives(
        @CurrentUser User user, @Valid GetRetrospectivesDto dto) {
        PaginationResponseDto<RetrospectiveResponseDto> response = retrospectiveService.getRetrospectives(
            user, dto);

        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @Operation(summary = "회고 단일 조회")
    @GetMapping("/{retrospectiveId}")
    public CommonApiResponse<GetRetrospectiveResponseDto> getRetrospective(@CurrentUser User user,
        @PathVariable Long retrospectiveId) {
        GetRetrospectiveResponseDto response = retrospectiveService.getRetrospective(user,
            retrospectiveId);

        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @Operation(summary = "회고 수정")
    @PutMapping("/{retrospectiveId}")
    public CommonApiResponse<RetrospectiveResponseDto> updateRetrospective(@CurrentUser User user,
        @PathVariable Long retrospectiveId, @RequestBody @Valid UpdateRetrospectiveDto dto) {
        RetrospectiveResponseDto response = retrospectiveService.updateRetrospective(user,
            retrospectiveId, dto);

        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @Operation(summary = "회고 생성")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CommonApiResponse<CreateRetrospectiveResponseDto> createRetrospective(
        @CurrentUser User user, @RequestBody @Valid CreateRetrospectiveDto createRetrospectiveDto) {
        CreateRetrospectiveResponseDto response = retrospectiveService.createRetrospective(user,
            createRetrospectiveDto);

        return CommonApiResponse.successResponse(HttpStatus.CREATED, response);

    }

    @Operation(summary = "회고 삭제")
    @DeleteMapping("/{retrospectiveId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRetrospective(@CurrentUser User user, @PathVariable Long retrospectiveId) {
        retrospectiveService.deleteRetrospective(retrospectiveId, user);
    }

    @Operation(summary = "회고 북마크")
    @PatchMapping("/{retrospectiveId}/bookmark")
    public CommonApiResponse<Boolean> toggleBookmark(@CurrentUser User user,
        @PathVariable Long retrospectiveId) {
        boolean isBookmarked = retrospectiveService.toggleBookmark(retrospectiveId, user);

        return CommonApiResponse.successResponse(HttpStatus.OK, isBookmarked);
    }

    @Operation(summary = "회고 리더 권한 양도")
    @PostMapping()
    public CommonApiResponse<CreateRetrospectiveResponseDto> transferLeaderdhip(@CurrentUser User user,
                                                                                @PathVariable Long retrospectiveId,
                                                                                @RequestParam Long newLeaderId) {
        CreateRetrospectiveResponseDto response = retrospectiveService.transferRetrospectiveLeadership(user, retrospectiveId, newLeaderId);
        return CommonApiResponse.successResponse(HttpStatus.CREATED, response);
    }

}
