package aws.retrospective.repository;

import aws.retrospective.entity.KudosTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KudosTargetRepository extends JpaRepository<KudosTarget, Long> {

    KudosTarget findBySectionId(Long sectionId);
    void deleteBySectionId(Long sectionId);
}
