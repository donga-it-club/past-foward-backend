package aws.retrospective.service;

import aws.retrospective.dto.GetTemplateSectionsDto;
import aws.retrospective.dto.RetrospectiveTemplateResponseDto;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.repository.RetrospectiveTemplateRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RetrospectiveTemplateService {

    private final RetrospectiveTemplateRepository retrospectiveTemplateRepository;
    private final TemplateSectionRepository templateSectionRepository;


    @Transactional(readOnly = true)
    public List<RetrospectiveTemplateResponseDto> getRetrospectiveTemplates() {
        List<RetrospectiveTemplate> templates = retrospectiveTemplateRepository.findAll();

        return templates.stream().map(
            template -> RetrospectiveTemplateResponseDto.builder().id(template.getId())
                .name(template.getName()).build()).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GetTemplateSectionsDto> getTemplateSections(Long templateId) {
        List<TemplateSection> templateSections = templateSectionRepository.findByTemplateId(
            templateId);

        return templateSections.stream().map(
            section -> GetTemplateSectionsDto.builder().id(section.getId())
                .name(section.getSectionName()).sequence(section.getSequence())
                .templateId(section.getTemplate().getId()).build()).collect(Collectors.toList());

    }


}
