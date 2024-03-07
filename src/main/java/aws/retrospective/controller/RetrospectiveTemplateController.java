package aws.retrospective.controller;


import aws.retrospective.dto.RetrospectiveTemplateResponseDto;
import aws.retrospective.service.RetrospectiveTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<RetrospectiveTemplateResponseDto> getRetrospectiveTemplates() {
        return retrospectiveTemplateService.getRetrospectiveTemplates();
    }

}
