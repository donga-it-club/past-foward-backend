package aws.retrospective.entity.redis;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("Notification")
public class NotificationRedis {

    @Id
    private String notification; // key
    private LocalDateTime lastNotificationTime; // value

    private NotificationRedis(String notification, LocalDateTime lastNotificationTime) {
        this.notification = notification;
        this.lastNotificationTime = lastNotificationTime;
    }

    public static NotificationRedis createNotification(String notification, LocalDateTime lastNotificationTime) {
        return new NotificationRedis(notification, lastNotificationTime);
    }
}
