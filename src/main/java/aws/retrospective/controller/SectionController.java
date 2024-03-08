package aws.retrospective.controller;

import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.dto.EditSectionRequestDto;
import aws.retrospective.dto.EditSectionResponseDto;
import aws.retrospective.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public CreateSectionResponseDto createSection(@RequestBody CreateSectionDto request) {
        return sectionService.createSection(request);
    }

    // 특정 섹션 수정
    @PutMapping("/{sectionId}")
    public EditSectionResponseDto editSectionContent(@PathVariable Long sectionId, @RequestBody EditSectionRequestDto request) {
        return sectionService.updateSectionContent(sectionId, request);
    }
}
