package aws.retrospective.repository;

import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.TemplateSection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {

    int countByRetrospectiveAndTemplateSection(Retrospective retrospective, TemplateSection templateSection);
}
