package aws.retrospective.repository;

import aws.retrospective.entity.ActionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {

}