package aws.retrospective.repository;

import aws.retrospective.entity.RetrospectiveGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RetrospectiveGroupRepository extends JpaRepository<RetrospectiveGroup, Long>,
        JpaSpecificationExecutor<RetrospectiveGroup> {

    Optional<RetrospectiveGroup> findRetrospectiveGroupById(@Param("retrospectiveGroupId") Long retrospectiveGroupId);
}