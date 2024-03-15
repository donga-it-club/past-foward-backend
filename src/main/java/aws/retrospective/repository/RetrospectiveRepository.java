package aws.retrospective.repository;

import aws.retrospective.entity.Retrospective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RetrospectiveRepository extends JpaRepository<Retrospective, Long>,
    JpaSpecificationExecutor<Retrospective> {

}
