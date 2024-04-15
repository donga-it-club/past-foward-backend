package aws.retrospective.repository;

import aws.retrospective.entity.TemplateSection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateSectionRepository extends JpaRepository<TemplateSection, Long> {

    List<TemplateSection> findByTemplateId(Long templateId);
}
