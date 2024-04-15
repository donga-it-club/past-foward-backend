package aws.retrospective.controller;


import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.GetTemplateSectionsDto;
import aws.retrospective.dto.RetrospectiveTemplateResponseDto;
import aws.retrospective.service.RetrospectiveTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/retrospective-templates")
@Tag(name = "retrospective-templates")
public class RetrospectiveTemplateController {

    private final RetrospectiveTemplateService retrospectiveTemplateService;

    @Operation(summary = "회고 템플릿 리스트 조회")
    @GetMapping()
    public CommonApiResponse<List<RetrospectiveTemplateResponseDto>> getRetrospectiveTemplates() {
        List<RetrospectiveTemplateResponseDto> response = retrospectiveTemplateService.getRetrospectiveTemplates();

        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @GetMapping("/{templateId}/template-sections")
    public CommonApiResponse<List<GetTemplateSectionsDto>> getTemplateSections(
        @PathVariable Long templateId) {
        List<GetTemplateSectionsDto> response = retrospectiveTemplateService.getTemplateSections(
            templateId);

        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

}
