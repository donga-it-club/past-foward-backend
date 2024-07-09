package aws.retrospective.repository;

import aws.retrospective.entity.Likes;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    List<Likes> deleteBySectionIdAndUserIdNotIn(Long sectionId, List<Long> userIds);

    boolean existsBySectionIdAndUserId(Long sectionId, Long userId);

}
