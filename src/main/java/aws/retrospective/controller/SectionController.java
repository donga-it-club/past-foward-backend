package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.common.CurrentUser;
import aws.retrospective.dto.AssignKudosRequestDto;
import aws.retrospective.dto.AssignKudosResponseDto;
import aws.retrospective.dto.AssignUserRequestDto;
import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.dto.EditSectionRequestDto;
import aws.retrospective.dto.EditSectionResponseDto;
import aws.retrospective.dto.GetCommentsResponseDto;
import aws.retrospective.dto.GetSectionsRequestDto;
import aws.retrospective.dto.GetSectionsResponseDto;
import aws.retrospective.dto.IncreaseSectionLikesResponseDto;
import aws.retrospective.dto.SectionNotificationDto;
import aws.retrospective.entity.User;
import aws.retrospective.service.CommentService;
import aws.retrospective.service.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sections")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@Tag(name = "회고 카드", description = "회고 카드 API<br>" + "200 OK : 요청을 정상적으로 처리<br>"
    + "201 Created : 요청을 정상적으로 처리하여 새로운 엔티티를 생성<br>"
    + "204 NoCont : 요청을 정상적으로 처리하였으나, 반환 할 데이터가 없음<br>"
    + "400 Error : Id로 엔티티를 조회 할 수 없을 때 발생하는 에러<br>")
public class SectionController {

    private final SectionService sectionService;
    private final CommentService commentService;

    // 회고 카드 등록
    @Operation(summary = "회고 카드 등록", description = "회고 카드 등록하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "201")})
    @PostMapping
    public CommonApiResponse<CreateSectionResponseDto> createSection(@CurrentUser User user,
        @Valid @RequestBody CreateSectionDto request) {
        CreateSectionResponseDto response = sectionService.createSection(user, request);
        return CommonApiResponse.successResponse(HttpStatus.CREATED, response);
    }

    // 특정 회고 카드 수정
    @Operation(summary = "회고 카드 수정", description = "등록 된 회고 카드의 내용을 수정하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @PatchMapping("/{sectionId}")
    public CommonApiResponse<EditSectionResponseDto> editSectionContent(@CurrentUser User user,
        @PathVariable Long sectionId, @Valid @RequestBody EditSectionRequestDto request) {
        EditSectionResponseDto response = sectionService.updateSectionContent(user, sectionId,
            request);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    // 회고 카드 좋아요
    @Operation(summary = "회고 카드 좋아요", description = "등록된 회고 카드의 좋아요 또는 취소 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @PostMapping("/{sectionId}/likes")
    public CommonApiResponse<IncreaseSectionLikesResponseDto> increaseSectionLikes(
        @CurrentUser User user, @PathVariable Long sectionId) {
        IncreaseSectionLikesResponseDto response = sectionService.increaseSectionLikes(sectionId,
            user);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    // 특정 회고 카드 삭제
    @Operation(summary = "회고 카드 삭제", description = "등록된 회고 카드를 삭제하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "204")})
    @DeleteMapping("/{sectionId}")
    public CommonApiResponse<Void> deleteSection(@CurrentUser User user,
        @PathVariable("sectionId") Long sectionId) {
        sectionService.deleteSection(sectionId, user);
        return CommonApiResponse.successResponse(HttpStatus.NO_CONTENT, null);
    }

    // 회고 카드 전체 조회
    @Operation(summary = "회고 카드 전체 조회", description = "회고보드 내의 회고 카드를 전체 조회하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping
    public CommonApiResponse<List<GetSectionsResponseDto>> getSections(
        @Valid GetSectionsRequestDto request) {
        List<GetSectionsResponseDto> response = sectionService.getSections(request);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    // Action Items 사용자 지정
    @Operation(summary = "Action Items 사용자 지정", description = "Action Items에 사용자를 지정하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "204")})
    @PutMapping("/action-items")
    public CommonApiResponse<Void> assignUser(@CurrentUser User user,
        @RequestBody @Valid AssignUserRequestDto request) {
        sectionService.assignUserToActionItem(request);
        return CommonApiResponse.successResponse(HttpStatus.NO_CONTENT, null);
    }

    @Operation(summary = "회고 카드 내 댓글 전체 조회", responses = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved comments"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{sectionId}/comments")
    public CommonApiResponse<List<GetCommentsResponseDto>> getComments(
        @CurrentUser User user,
        @PathVariable("sectionId") Long sectionId
    ) {
        List<GetCommentsResponseDto> response = commentService.getComments(user, sectionId);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @Operation(summary = "새로운 댓글 및 좋아요 조회", description = "새로운 댓글과 좋아요를 조회하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 알림이 조회되었습니다.")
    })
    @GetMapping("/notifications")
    public CommonApiResponse<List<SectionNotificationDto>> getNewComments() {
        List<SectionNotificationDto> result = sectionService.getNewCommentsAndLikes();
        return CommonApiResponse.successResponse(HttpStatus.OK, result);
    }

    @Operation(summary = "Kudos 템플릿에 사용자 지정", description = "Kudos 유형의 회고 카드에 칭찬할 사용자를 지정하는 API ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200")
    })
    @PutMapping("/{sectionId}/kudos-target")
    public CommonApiResponse<AssignKudosResponseDto> assignKudosPerson(@PathVariable Long sectionId, @Valid @RequestBody AssignKudosRequestDto request) {
        AssignKudosResponseDto response = sectionService.assignKudos(sectionId, request);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

}
