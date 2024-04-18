package aws.retrospective.repository;

import aws.retrospective.entity.Retrospective;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RetrospectiveRepository extends JpaRepository<Retrospective, Long>,
    JpaSpecificationExecutor<Retrospective> {

    @Query("select r from Retrospective r"
        + " left join fetch r.team t"
        + " join fetch r.user u"
        + " join fetch r.template rt"
        + " where r.id = :retrospectiveId")
    Optional<Retrospective> findRetrospectiveById(@Param("retrospectiveId") Long retrospectiveId);
}
