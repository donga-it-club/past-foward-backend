package aws.retrospective.repository;

import aws.retrospective.entity.RetrospectiveGroup;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RetrospectiveGroupRepository extends JpaRepository<RetrospectiveGroup, Long>,
    JpaSpecificationExecutor<RetrospectiveGroup> {

    Page<RetrospectiveGroup> findByGroupIdAndUserId(Long GroupId, Long userId, Pageable pageable);

    @Query("select rg from RetrospectiveGroup rg"
        + "join fetch rg.user u"
        + "where rg.id = :retrospectiveGroupID")
    Optional<RetrospectiveGroup> findRetrospectiveGroupById(
        @Param("retrospectiveGroupId") Long retrospectiveGroupId);
}
