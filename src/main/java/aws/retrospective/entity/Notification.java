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
import lombok.Builder;
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
    @JoinColumn(name = "sender_id")
    private User sender; // 알림을 발생시킨 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver; // 알림을 받는 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "likes_id")
    private Likes likes;

    @Enumerated(EnumType.STRING)
    private NotificationStatus isRead; // UNREAD, READ

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType; // COMMENT, LIKE

    @Builder
    private Notification(Section section, Retrospective retrospective, User sender, User receiver,
        Comment comment,
        Likes likes, NotificationType notificationType) {
        this.section = section;
        this.retrospective = retrospective;
        this.sender = sender;
        this.receiver = receiver;
        this.comment = comment;
        this.likes = likes;
        this.isRead = NotificationStatus.UNREAD;
        this.notificationType = notificationType;
    }

    public static Notification of(Section section, Retrospective retrospective,
        User sender, User receiver, Comment comment, Likes likes,
        NotificationType notificationType) {
        return new Notification(section, retrospective, sender, receiver, comment, likes,
            notificationType);
    }

    public void readNotification() {
        this.isRead = NotificationStatus.READ;
    }
}
