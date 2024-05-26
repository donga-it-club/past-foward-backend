package aws.retrospective.entity;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("Notification")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationRedis {

    @Id
    private String notification; // key
    private LocalDateTime lastNotificationTime; // value

    public static NotificationRedis of(String notification, LocalDateTime lastNotificationTime) {
        return new NotificationRedis(notification, lastNotificationTime);
    }
}
