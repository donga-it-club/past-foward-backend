package aws.retrospective.repository;

import aws.retrospective.dto.GetSectionsResponseDto;
import aws.retrospective.entity.Section;
import java.util.List;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.TemplateSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SectionRepository extends JpaRepository<Section, Long>, SectionRepositoryCustom {

    int countByRetrospectiveAndTemplateSection(Retrospective retrospective, TemplateSection templateSection);
}
