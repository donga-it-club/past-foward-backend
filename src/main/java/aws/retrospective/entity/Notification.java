package aws.retrospective.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retrospective_id")
    private Retrospective retrospective;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser; // 이벤트 발생시킨 사람

    // 알림을 받는 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "likes_id")
    private Likes likes;

    @Enumerated(EnumType.STRING)
    private NotificationStatus isRead; // 0: unread, 1: read

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType; // COMMENT, LIKE

    private Notification(Section section, Retrospective retrospective, User fromUser, User toUser, Comment comment,
        Likes likes, NotificationType notificationType) {
        this.section = section;
        this.retrospective = retrospective;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.comment = comment;
        this.likes = likes;
        this.isRead = NotificationStatus.UNREAD;
        this.notificationType = notificationType;
    }

    public static Notification createNotification(Section section, Retrospective retrospective,
        User fromUser, User toUser, Comment comment, Likes likes, NotificationType notificationType) {
        return new Notification(section, retrospective, fromUser, toUser, comment, likes,
            notificationType);
    }

    public void readNotification() {
        this.isRead = NotificationStatus.READ;
    }
}
