package aws.retrospective.controller;


import aws.retrospective.dto.RetrospectiveTemplateResponseDto;
import aws.retrospective.service.RetrospectiveTemplateService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/retrospective-template")
public class RetrospectiveTemplateController {

    private final RetrospectiveTemplateService retrospectiveTemplateService;

    public RetrospectiveTemplateController(
        RetrospectiveTemplateService retrospectiveTemplateService) {
        this.retrospectiveTemplateService = retrospectiveTemplateService;
    }

    @GetMapping()
    public List<RetrospectiveTemplateResponseDto> getRetrospectiveTemplates() {
        return retrospectiveTemplateService.getRetrospectiveTemplates();
    }

}