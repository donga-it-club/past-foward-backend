package aws.retrospective.repository;

import aws.retrospective.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Bookmark findByUserIdAndRetrospectiveId(Long userId, Long retrospectiveId);
}
