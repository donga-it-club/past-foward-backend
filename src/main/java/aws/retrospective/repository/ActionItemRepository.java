package aws.retrospective.repository;

import aws.retrospective.entity.ActionItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {

    void deleteBySectionId(Long sectionId);
}