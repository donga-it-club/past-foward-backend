package aws.retrospective.factory;

import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;

public interface SectionFactory {

    Section createSection(String content, Retrospective retrospective,
        TemplateSection templateSection, User user);

}
