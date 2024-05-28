package aws.retrospective.repository;

import aws.retrospective.entity.Notification;
import aws.retrospective.entity.NotificationStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByIsReadOrCreatedDateAfter(NotificationStatus isRead,
        LocalDateTime createdDate);

    Optional<Notification> findNotificationByCommentId(Long commentId);

    Optional<Notification> findNotificationByLikesId(Long likesId);
}
