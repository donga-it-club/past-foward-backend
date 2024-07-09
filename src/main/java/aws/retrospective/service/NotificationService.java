package aws.retrospective.service;

import aws.retrospective.dto.GetNotificationResponseDto;
import aws.retrospective.entity.Notification;
import aws.retrospective.entity.NotificationStatus;
import aws.retrospective.entity.NotificationRedis;
import aws.retrospective.entity.User;
import aws.retrospective.repository.NotificationRedisRepository;
import aws.retrospective.repository.NotificationRepository;
import aws.retrospective.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationRedisRepository redisRepository;
    private final UserRepository userRepository;

    private static final String NOTIFICATION = "notification";

    @Transactional
    public void readNotification(Long notificationId) {
        Notification notification = getNotification(notificationId);
        notification.readNotification();
    }

    @Transactional(readOnly = true)
    public List<GetNotificationResponseDto> getNotifications() {
        // 알림 조회
        NotificationRedis notificationRedis = findNotification();

        // Redis에 현재 시간(마지막으로 조회한) 시간을 저장
        NotificationRedis notification = createNotification();
        redisRepository.save(notification);

        return convertDto(notificationRedis);
    }

    // 알림 삭제
    @Transactional
    public void deleteNotificationAll(Long userId) {
        User user = findUserById(userId); // 알림을 받은 사용자 조회

        readNotificationAllByUserId(user); // 사용자가 받은 알림을 모두 읽음 처리
    }

    private void readNotificationAllByUserId(User user) {
        notificationRepository.readNotificationAll(NotificationStatus.READ,
            NotificationStatus.UNREAD, user.getId());
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("사용자를 조회할 수 없습니다. Id : " + userId));
    }

    private NotificationRedis findNotification() {
        return redisRepository.findById(NOTIFICATION)
            .orElse(createNotification());
    }

    private static NotificationRedis createNotification() {
        return NotificationRedis.of(NOTIFICATION, LocalDateTime.now());
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
