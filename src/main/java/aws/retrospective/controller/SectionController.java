package aws.retrospective.controller;

import aws.retrospective.common.ApiResponse;
import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.dto.EditSectionRequestDto;
import aws.retrospective.dto.EditSectionResponseDto;
import aws.retrospective.dto.IncreaseSectionLikesRequestDto;
import aws.retrospective.dto.IncreaseSectionLikesResponseDto;
import aws.retrospective.exception.ErrorResponse;
import aws.retrospective.service.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sections")
@RequiredArgsConstructor
@Tag(name = "Section", description = "Section API")
public class SectionController {

    private final SectionService sectionService;

    // 특정 섹션 추가
    @Operation(summary = "Section 추가", description = "회고보드 내의 section을 등록하는 API")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "successful operation"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "bad request operation")})
    @PostMapping
    public ApiResponse<CreateSectionResponseDto> createSection(
        @Valid @RequestBody CreateSectionDto request) {
        CreateSectionResponseDto response = sectionService.createSection(request);
        return ApiResponse.successResponse(HttpStatus.CREATED, response);
    }

    // 특정 섹션 수정
    @Operation(summary = "Section 수정", description = "등록 된 section의 내용을 수정하는 API")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successful operation"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping("/{sectionId}")
    public ApiResponse<EditSectionResponseDto> editSectionContent(@PathVariable Long sectionId, @Valid @RequestBody EditSectionRequestDto request) {
        EditSectionResponseDto response = sectionService.updateSectionContent(
            sectionId, request);
        return ApiResponse.successResponse(HttpStatus.OK, response);
    }

    // 섹션 좋아요
    @Operation(summary = "Section 좋아요", description = "등록된 section의 좋아요 또는 취소 API")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successful operation")})
    @PostMapping("/{sectionId}/likes")
    public ApiResponse<IncreaseSectionLikesResponseDto> increaseSectionLikes(@PathVariable Long sectionId, @Valid @RequestBody IncreaseSectionLikesRequestDto request) {
        IncreaseSectionLikesResponseDto response = sectionService.increaseSectionLikes(
            sectionId, request);
        return ApiResponse.successResponse(HttpStatus.OK, response);
    }

    // 특정 섹션 삭제
    @Operation(summary = "Section 삭제", description = "등록된 section을 삭제하는 API")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "successful operation"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "bad request operation")})
    @DeleteMapping("/{sectionId}")
    public ApiResponse<Void> deleteSection(@PathVariable("sectionId") Long sectionId) {
        sectionService.deleteSection(sectionId);
        return ApiResponse.successResponse(HttpStatus.NO_CONTENT, null);
    }
}
