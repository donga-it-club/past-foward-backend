package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.dto.DeleteSectionRequestDto;
import aws.retrospective.dto.EditSectionRequestDto;
import aws.retrospective.dto.EditSectionResponseDto;
import aws.retrospective.dto.IncreaseSectionLikesRequestDto;
import aws.retrospective.dto.IncreaseSectionLikesResponseDto;
import aws.retrospective.service.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "Section", description = "Section API<br>"
    + "200 OK : 요청을 정상적으로 처리<br>"
    + "201 Created : 요청을 정상적으로 처리하여 새로운 엔티티를 생성<br>"
    + "204 NoCont : 요청을 정상적으로 처리하였으나, 반환 할 데이터가 없음<br>"
    + "400 Error : Id로 엔티티를 조회 할 수 없을 때 발생하는 에러<br>")
public class SectionController {

    private final SectionService sectionService;

    // 특정 섹션 추가
    @Operation(summary = "Section 등록", description = "회고보드 내의 section을 등록하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201")})
    @PostMapping
    public CommonApiResponse<CreateSectionResponseDto> createSection(
        @Valid @RequestBody CreateSectionDto request) {
        CreateSectionResponseDto response = sectionService.createSection(request);
        return CommonApiResponse.successResponse(HttpStatus.CREATED, response);
    }

    // 특정 섹션 수정
    @Operation(summary = "Section 수정", description = "등록 된 section의 내용을 수정하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200")})
    @PatchMapping("/{sectionId}")
    public CommonApiResponse<EditSectionResponseDto> editSectionContent(@PathVariable Long sectionId, @Valid @RequestBody EditSectionRequestDto request) {
        EditSectionResponseDto response = sectionService.updateSectionContent(
            sectionId, request);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    // 섹션 좋아요
    @Operation(summary = "Section 좋아요", description = "등록된 section의 좋아요 또는 취소 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200")})
    @PostMapping("/{sectionId}/likes")
    public CommonApiResponse<IncreaseSectionLikesResponseDto> increaseSectionLikes(@PathVariable Long sectionId, @Valid @RequestBody IncreaseSectionLikesRequestDto request) {
        IncreaseSectionLikesResponseDto response = sectionService.increaseSectionLikes(
            sectionId, request);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    // 특정 섹션 삭제
    @Operation(summary = "Section 삭제", description = "등록된 section을 삭제하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204")})
    @DeleteMapping("/{sectionId}")
    public CommonApiResponse<Void> deleteSection(@PathVariable("sectionId") Long sectionId, @Valid @RequestBody DeleteSectionRequestDto request) {
        sectionService.deleteSection(sectionId, request);
        return CommonApiResponse.successResponse(HttpStatus.NO_CONTENT, null);
    }
}
