package aws.retrospective.service;

import aws.retrospective.dto.RetrospectiveTemplateResponseDto;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.repository.RetrospectiveTemplateRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetrospectiveTemplateService {

    private final RetrospectiveTemplateRepository retrospectiveTemplateRepository;


    public List<RetrospectiveTemplateResponseDto> getRetrospectiveTemplates() {
        List<RetrospectiveTemplate> templates = retrospectiveTemplateRepository
            .findAll();

        return templates.stream()
            .map(template -> RetrospectiveTemplateResponseDto.builder()
                .id(template.getId())
                .name(template.getName())
                .build())
            .collect(Collectors.toList());
    }

}
