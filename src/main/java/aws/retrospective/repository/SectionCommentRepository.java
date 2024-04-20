package aws.retrospective.repository;

import aws.retrospective.entity.SectionComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionCommentRepository extends JpaRepository<SectionComment, Long> {

    List<SectionComment> findBySectionId(Long sectionId);
}