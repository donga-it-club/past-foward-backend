package aws.retrospective.service;


import static org.assertj.core.api.Assertions.assertThat;

import aws.retrospective.dto.RetrospectiveTemplateResponseDto;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.repository.RetrospectiveTemplateRepository;
import java.util.List;
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

}
