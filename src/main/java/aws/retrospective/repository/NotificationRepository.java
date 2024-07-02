package aws.retrospective.repository;

import aws.retrospective.entity.Notification;
import aws.retrospective.entity.NotificationStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByIsReadOrCreatedDateAfter(NotificationStatus isRead,
        LocalDateTime createdDate);

    Optional<Notification> findNotificationByCommentId(Long commentId);

    Optional<Notification> findNotificationByLikesId(Long likesId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Notification SET isRead = :readStatus WHERE isRead = :unReadStatus AND receiver.id = :userId")
    void readNotificationAll(@Param("readStatus") NotificationStatus readStatus,
        @Param("unReadStatus") NotificationStatus unReadStatus,
        @Param("userId") Long userId);
}
