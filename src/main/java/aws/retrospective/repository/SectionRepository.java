package aws.retrospective.repository;

import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.TemplateSection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SectionRepository extends JpaRepository<Section, Long>, SectionRepositoryCustom {

    int countByRetrospectiveAndTemplateSection(Retrospective retrospective, TemplateSection templateSection);

    @Query("select s from Section s join fetch s.retrospective sr where sr.id = :retrospectiveId")
    List<Section> getSectionsWithComments(@Param("retrospectiveId") Long retrospectiveId);
}
