package aws.retrospective.repository;

import aws.retrospective.dto.GetSectionsResponseDto;
import aws.retrospective.entity.Section;
import java.util.List;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.TemplateSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query("select new aws.retrospective"
        + ".dto.GetSectionsResponseDto(s.id, u.username, s.content, s.likeCnt, ts.sectionName, s.createdDate)"
        + " from Section s"
        + " join s.retrospective r"
        + " join s.user u"
        + " join s.templateSection ts"
        + " where r.id = :retrospectiveId")
    List<GetSectionsResponseDto> findSections(Long retrospectiveId);
    int countByRetrospectiveAndTemplateSection(Retrospective retrospective, TemplateSection templateSection);
}
