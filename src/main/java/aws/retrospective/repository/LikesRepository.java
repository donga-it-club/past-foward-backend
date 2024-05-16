package aws.retrospective.repository;

import aws.retrospective.entity.Likes;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByUserAndSection(User user, Section section);

    Likes findTopBySectionIdOrderByCreatedDateDesc(Long sectionId);

    default Likes findLatestLikeBySection(Long sectionId) {
        return findTopBySectionIdOrderByCreatedDateDesc(sectionId);
    }

    List<Likes> findBySectionIdAndCreatedDateAfterOrderByCreatedDateDesc(Long sectionId,
        LocalDateTime createdDate);

    default List<Likes> findLikesAfterDate(Long sectionId, LocalDateTime createdDate) {
        return findBySectionIdAndCreatedDateAfterOrderByCreatedDateDesc(sectionId, createdDate);
    }

    List<Likes> findAllBySectionIdOrderByCreatedDateDesc(Long sectionId);

    default List<Likes> findAllLikes(Long sectionId) {
        return findAllBySectionIdOrderByCreatedDateDesc(sectionId);
    }
}
