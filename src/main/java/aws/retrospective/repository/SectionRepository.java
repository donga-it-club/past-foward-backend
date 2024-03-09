package aws.retrospective.repository;

import aws.retrospective.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {

}
