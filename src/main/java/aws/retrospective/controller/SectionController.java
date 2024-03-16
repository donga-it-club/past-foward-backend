package aws.retrospective.controller;

import aws.retrospective.common.ApiResponse;
import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.dto.EditSectionRequestDto;
import aws.retrospective.dto.EditSectionResponseDto;
import aws.retrospective.service.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ApiResponse<CreateSectionResponseDto> createSection(
        @Valid @RequestBody CreateSectionDto request) {
        CreateSectionResponseDto response = sectionService.createSection(request);
        return ApiResponse.successResponse(HttpStatus.CREATED, response);
    }

    // 특정 섹션 수정
    @PatchMapping("/{sectionId}")
    public ApiResponse<EditSectionResponseDto> editSectionContent(@PathVariable Long sectionId, @Valid @RequestBody EditSectionRequestDto request) {
        EditSectionResponseDto response = sectionService.updateSectionContent(
            sectionId, request);
        return ApiResponse.successResponse(HttpStatus.OK, response);
    }
}
