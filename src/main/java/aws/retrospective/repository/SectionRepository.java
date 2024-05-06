package aws.retrospective.repository;

import aws.retrospective.entity.Section;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SectionRepository extends JpaRepository<Section, Long>, SectionRepositoryCustom {

    @Query("select s from Section s join fetch s.retrospective sr left join fetch s.actionItem ac left join fetch s.comments sc where sr.id = :retrospectiveId")
    List<Section> getSectionsWithComments(@Param("retrospectiveId") Long retrospectiveId);
}
