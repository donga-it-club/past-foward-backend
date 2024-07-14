package aws.retrospective.repository;

import aws.retrospective.entity.Notification;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepositoryCustom {

    List<Notification> findUnReadAndNewNotifications(LocalDateTime lastNotificationTime);
}
