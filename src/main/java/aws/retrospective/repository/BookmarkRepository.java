package aws.retrospective.repository;

import aws.retrospective.entity.Bookmark;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByUserIdAndRetrospectiveId(Long userId, Long retrospectiveId);
}
