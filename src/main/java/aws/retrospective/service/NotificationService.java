package aws.retrospective.service;

import aws.retrospective.dto.GetNotificationResponseDto;
import aws.retrospective.entity.Notification;
import aws.retrospective.entity.NotificationStatus;
import aws.retrospective.entity.NotificationRedis;
import aws.retrospective.repository.NotificationRedisRepository;
import aws.retrospective.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationRedisRepository redisRepository;

    private static final String NOTIFICATION = "notification";

    @Transactional
    public void readNotification(Long notificationId) {
        Notification notification = getNotification(notificationId);
        notification.readNotification();
    }

    @Transactional(readOnly = true)
    public List<GetNotificationResponseDto> getNotifications() {
        NotificationRedis notificationRedis = findNotification();

        NotificationRedis notification = createNotification();
        redisRepository.save(notification);

        return convertDto(notificationRedis);
    }

    private NotificationRedis findNotification() {
        return redisRepository.findById(NOTIFICATION)
            .orElseThrow(() -> new NoSuchElementException("알림을 조회할 수 없습니다. ID: " + NOTIFICATION));
    }

    private static NotificationRedis createNotification() {
        return NotificationRedis.of(NOTIFICATION,
            LocalDateTime.now());
    }

    private List<GetNotificationResponseDto> convertDto(NotificationRedis notificationRedis) {
        return notificationRepository.findByIsReadOrCreatedDateAfter(
                NotificationStatus.UNREAD, notificationRedis.getLastNotificationTime()).stream()
            .map(GetNotificationResponseDto::of)
            .toList();
    }

    private Notification getNotification(Long notificationId) {
        return notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NoSuchElementException("알림을 조회할 수 없습니다. ID: " + notificationId));
    }
}
