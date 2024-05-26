package aws.retrospective.dto;

import aws.retrospective.entity.Notification;
import aws.retrospective.entity.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GetNotificationResponseDto {

    @Schema(description = "알림 ID", example = "1")
    private Long notificationId;
    @Schema(description = "이벤트가 발생한 회고보드 ID", example = "1")
    private Long sectionId;
    @Schema(description = "회고 제목", example = "중간 회고")
    private String retrospectiveTitle;
    @Schema(description = "알림을 받는 사용자 ID", example = "1")
    private Long toUserId;
    @Schema(description = "알림을 발생시킨 사용자 이름", example = "test")
    private String fromUsername;
    @Schema(description = "이벤트를 발생시킨 사용자 프로필 이미지", example = "4728e325-6215-40ba-abd8-7bf26bcd9029")
    private String thumbnail;
    @Schema(description = "알림 타입", example = "COMMENT")
    private NotificationType notificationType;
    @Schema(description = "알림 발생 시간", example = "2021-07-01T00:00:00")
    private LocalDateTime dateTime;

    private GetNotificationResponseDto(Long notificationId, Long sectionId,
        String retrospectiveTitle, Long toUserId,
        String fromUsername, String thumbnail, NotificationType notificationType,
        LocalDateTime dateTime) {
        this.notificationId = notificationId;
        this.sectionId = sectionId;
        this.retrospectiveTitle = retrospectiveTitle;
        this.toUserId = toUserId;
        this.fromUsername = fromUsername;
        this.thumbnail = thumbnail;
        this.notificationType = notificationType;
        this.dateTime = dateTime;
    }

    public static GetNotificationResponseDto of(Notification notification) {
        return new GetNotificationResponseDto(notification.getId(),
            notification.getSection().getId(),
            notification.getRetrospective().getTitle(), notification.getToUser().getId(),
            notification.getFromUser().getUsername(), notification.getFromUser().getThumbnail(),
            notification.getNotificationType(), notification.getCreatedDate());
    }
}
