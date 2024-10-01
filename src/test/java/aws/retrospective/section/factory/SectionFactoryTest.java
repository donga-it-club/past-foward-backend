package aws.retrospective.section.factory;

import static org.assertj.core.api.Assertions.assertThat;

import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import aws.retrospective.factory.SectionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SectionFactoryTest {

    @Autowired
    private SectionFactory sectionFactory;

    @Test
    @DisplayName("회고카드를 생성할 수 있다.")
    void createSection() {
        //given
        String content = "test";
        User user = User.builder().build();
        RetrospectiveTemplate retrospectiveTemplate = createRetrospectiveTemplate("title");
        Retrospective retrospective = createRetrospective("title", retrospectiveTemplate);
        TemplateSection templateSection = createTemplateSection("Keep");

        //when
        Section section = sectionFactory.createSection(content, retrospective, templateSection,
            user);

        //then
        assertThat(section.getId()).isNull();
        assertThat(section.getContent()).isEqualTo("test");
        assertThat(section.getRetrospective()).isEqualTo(retrospective);
        assertThat(section.getTemplateSection()).isEqualTo(templateSection);
    }

    private static TemplateSection createTemplateSection(String sectionName) {
        return TemplateSection.builder()
            .sectionName(sectionName)
            .sequence(0)
            .build();
    }

    private static Retrospective createRetrospective(String title,
        RetrospectiveTemplate retrospectiveTemplate) {
        return Retrospective.builder()
            .title(title)
            .template(retrospectiveTemplate)
            .build();
    }

    private static RetrospectiveTemplate createRetrospectiveTemplate(String templateName) {
        return RetrospectiveTemplate.builder()
            .name(templateName)
            .build();
    }

}