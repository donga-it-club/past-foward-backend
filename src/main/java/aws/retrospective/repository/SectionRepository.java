package aws.retrospective.repository;

import aws.retrospective.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query(value = "SELECT COUNT(s.templateSection.sectionName) FROM Section s")
    int findSectionSequence(@Param("sectionName") String sectionName);

    @Modifying
    @Query(value = "UPDATE Section s SET s.content = :updateContent where s.id = :sectionId")
    void updateSectionContent(@Param("updateContent") String content, @Param("sectionId") Long id);
}
