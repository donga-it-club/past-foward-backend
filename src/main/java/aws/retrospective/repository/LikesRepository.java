package aws.retrospective.repository;

import aws.retrospective.entity.Likes;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByUserAndSection(User user, Section section);
}
