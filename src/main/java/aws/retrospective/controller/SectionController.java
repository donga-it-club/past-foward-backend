package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.dto.EditSectionRequestDto;
import aws.retrospective.dto.EditSectionResponseDto;
import aws.retrospective.dto.IncreaseSectionLikesRequestDto;
import aws.retrospective.dto.IncreaseSectionLikesResponseDto;
import aws.retrospective.service.SectionService;
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
public class SectionController {

    private final SectionService sectionService;

    // 특정 섹션 추가
    @PostMapping
    public CommonApiResponse<CreateSectionResponseDto> createSection(
        @Valid @RequestBody CreateSectionDto request) {
        CreateSectionResponseDto response = sectionService.createSection(request);
        return CommonApiResponse.successResponse(HttpStatus.CREATED, response);
    }

    // 특정 섹션 수정
    @PatchMapping("/{sectionId}")
    public CommonApiResponse<EditSectionResponseDto> editSectionContent(@PathVariable Long sectionId, @Valid @RequestBody EditSectionRequestDto request) {
        EditSectionResponseDto response = sectionService.updateSectionContent(
            sectionId, request);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    // 섹션 좋아요
    @PostMapping("/{sectionId}/likes")
    public CommonApiResponse<IncreaseSectionLikesResponseDto> increaseSectionLikes(@PathVariable Long sectionId, @Valid @RequestBody IncreaseSectionLikesRequestDto request) {
        IncreaseSectionLikesResponseDto response = sectionService.increaseSectionLikes(
            sectionId, request);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }
  
    // 특정 섹션 삭제
    @DeleteMapping("/{sectionId}")
    public CommonApiResponse<Void> deleteSection(@PathVariable("sectionId") Long sectionId) {
        sectionService.deleteSection(sectionId);
        return CommonApiResponse.successResponse(HttpStatus.NO_CONTENT, null);
    }
}
