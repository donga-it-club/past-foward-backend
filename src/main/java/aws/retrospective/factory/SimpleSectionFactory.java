package aws.retrospective.factory;

import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import org.springframework.stereotype.Component;

@Component
public class SimpleSectionFactory implements SectionFactory {

    @Override
    public Section createSection(String content, Retrospective retrospective,
        TemplateSection templateSection, User user) {
        return Section.create(content, retrospective, user, templateSection);
    }

}
