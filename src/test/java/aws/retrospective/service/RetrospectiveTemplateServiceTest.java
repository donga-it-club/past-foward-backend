package aws.retrospective.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import aws.retrospective.dto.GetTemplateSectionsDto;
import aws.retrospective.dto.RetrospectiveTemplateResponseDto;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.repository.RetrospectiveTemplateRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class RetrospectiveTemplateServiceTest {


    @Mock
    private RetrospectiveTemplateRepository retrospectiveTemplateRepository;

    @Mock
    private TemplateSectionRepository templateSectionRepository;


    @InjectMocks
    private RetrospectiveTemplateService retrospectiveTemplateService;

    @Test
    void getRetrospectiveTemplates() {
        RetrospectiveTemplate fitstRetrospectiveTemplate = new RetrospectiveTemplate(
            "Template Name 1");
        RetrospectiveTemplate secondRetrospectiveTemplate = new RetrospectiveTemplate(
            "Template Name 2");

        ReflectionTestUtils.setField(fitstRetrospectiveTemplate, "id", 1L);
        ReflectionTestUtils.setField(secondRetrospectiveTemplate, "id", 2L);

        BDDMockito.given(retrospectiveTemplateRepository.findAll())
            .willReturn(List.of(fitstRetrospectiveTemplate, secondRetrospectiveTemplate));

        // when
        List<RetrospectiveTemplateResponseDto> retrospectiveTemplates = retrospectiveTemplateService
            .getRetrospectiveTemplates();

        // then
        assertThat(retrospectiveTemplates).hasSize(2);
        assertThat(retrospectiveTemplates.get(0).getId()).isEqualTo(1L);
        assertThat(retrospectiveTemplates.get(0).getName()).isEqualTo("Template Name 1");
        assertThat(retrospectiveTemplates.get(1).getId()).isEqualTo(2L);
        assertThat(retrospectiveTemplates.get(1).getName()).isEqualTo("Template Name 2");


    }

    @Test
    void getTemplateSections() {
        // given
        Long templateId = 1L;
        RetrospectiveTemplate template = new RetrospectiveTemplate("Template Name");
        ReflectionTestUtils.setField(template, "id", templateId);

        List<TemplateSection> templateSections = IntStream.range(0, 3).mapToObj(i ->
            TemplateSection.builder()
                .template(template)
                .sectionName("Section " + i)
                .sequence(i)
                .build()
        ).collect(Collectors.toList());

        given(templateSectionRepository.findByTemplateId(templateId)).willReturn(templateSections);

        // when
        List<GetTemplateSectionsDto> result = retrospectiveTemplateService.getTemplateSections(
            templateId);

        // then
        assertThat(result).hasSize(3);
        IntStream.range(0, 3).forEach(i -> {
            assertThat(result.get(i).getId()).isNull();
            assertThat(result.get(i).getName()).isEqualTo("Section " + i);
            assertThat(result.get(i).getSequence()).isEqualTo(i);
            assertThat(result.get(i).getTemplateId()).isEqualTo(templateId);
        });

    }

}
