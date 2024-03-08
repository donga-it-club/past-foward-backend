package aws.retrospective.repository;

import aws.retrospective.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Long countByTemplateSectionSectionName(String sectionName);
}
