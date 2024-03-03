package aws.retrospective.service;

import aws.retrospective.dto.RetrospectiveTemplateResponseDto;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.repository.RetrospectiveTemplateRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetrospectiveTemplateService {

    private final RetrospectiveTemplateRepository retrospectiveTemplateRepository;


    @Autowired
    public RetrospectiveTemplateService(
        RetrospectiveTemplateRepository retrospectiveTemplateRepository) {
        this.retrospectiveTemplateRepository = retrospectiveTemplateRepository;
    }

    public List<RetrospectiveTemplateResponseDto> getRetrospectiveTemplates() {
        List<RetrospectiveTemplate> templates = retrospectiveTemplateRepository
            .findAll();

        return templates.stream()
            .map(template -> {
                RetrospectiveTemplateResponseDto dto = new RetrospectiveTemplateResponseDto();
                dto.setId(template.getId());
                dto.setName(template.getName());
                return dto;
            })
            .collect(Collectors.toList());
    }

}
