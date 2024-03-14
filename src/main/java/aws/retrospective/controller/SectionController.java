package aws.retrospective.controller;

import aws.retrospective.common.ApiResponse;
import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.dto.DeleteSectionResponseDto;
import aws.retrospective.service.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public CreateSectionResponseDto createSection(@Valid @RequestBody CreateSectionDto request) {
        return sectionService.createSection(request);
    }

    // 특정 섹션 삭제
    @DeleteMapping("/{id}")
    public ApiResponse<DeleteSectionResponseDto> deleteSection(@PathVariable("id") Long sectionId) {
        DeleteSectionResponseDto response = sectionService.deleteSection(sectionId);
        return ApiResponse.successResponse(HttpStatus.OK, response);
    }
}
