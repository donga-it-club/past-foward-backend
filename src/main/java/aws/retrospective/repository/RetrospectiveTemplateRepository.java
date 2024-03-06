package aws.retrospective.repository;

import aws.retrospective.entity.RetrospectiveTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetrospectiveTemplateRepository extends
    JpaRepository<RetrospectiveTemplate, Long> {

}
