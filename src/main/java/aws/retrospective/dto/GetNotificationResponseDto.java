package aws.retrospective.dto;

import aws.retrospective.entity.Notification;
import aws.retrospective.entity.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private Long receiverId;
    @Schema(description = "알림을 발생시킨 사용자 이름", example = "test")
    private String senderName;
    @Schema(description = "이벤트를 발생시킨 사용자 프로필 이미지", example = "4728e325-6215-40ba-abd8-7bf26bcd9029")
    private String thumbnail;
    @Schema(description = "알림 타입", example = "COMMENT")
    private NotificationType notificationType;
    @Schema(description = "알림 발생 시간", example = "2021-07-01T00:00:00")
    private LocalDateTime dateTime;
    @Schema(description = "회고 보드 ID", example = "1")
    private Long retrospectiveId;
    @Schema(description = "팀 ID", example = "1")
    private Long teamId;

    private GetNotificationResponseDto(Long notificationId, Long sectionId,
        String retrospectiveTitle, Long receiverId,
        String senderName, String thumbnail, NotificationType notificationType,
        LocalDateTime dateTime, Long retrospectiveId, Long teamId) {
        this.notificationId = notificationId;
        this.sectionId = sectionId;
        this.retrospectiveTitle = retrospectiveTitle;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.thumbnail = thumbnail;
        this.notificationType = notificationType;
        this.dateTime = parseDateTime(dateTime.toString());
        this.retrospectiveId = retrospectiveId;
        this.teamId = teamId;
    }

    public static GetNotificationResponseDto of(Notification notification) {
        return new GetNotificationResponseDto(notification.getId(),
            notification.getSection().getId(),
            notification.getRetrospective().getTitle(), notification.getReceiver().getId(),
            notification.getSender().getUsername(), notification.getSender().getThumbnail(),
            notification.getNotificationType(), notification.getCreatedDate(),
            notification.getRetrospective().getId(),
            notification.getRetrospective().getTeam() == null ? null
                : notification.getRetrospective().getTeam().getId());
    }

    private LocalDateTime parseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
