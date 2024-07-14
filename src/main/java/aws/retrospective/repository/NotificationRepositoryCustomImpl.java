package aws.retrospective.repository;

import static aws.retrospective.entity.QNotification.notification;
import static aws.retrospective.entity.QRetrospective.retrospective;

import aws.retrospective.entity.Notification;
import aws.retrospective.entity.NotificationStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {

    private final JPQLQueryFactory queryFactory;

    @Override
    public List<Notification> findUnReadAndNewNotifications(LocalDateTime lastNotificationTime) {
        return queryFactory
            .selectFrom(notification)
            .where(eqNotDeletedRetrospective().or(eqUnReadNotification())
                .or(gtNotificationTime(lastNotificationTime)))
            .fetch();
    }

    private BooleanExpression eqNotDeletedRetrospective() {
        return retrospective.deletedDate.isNotNull();
    }

    private BooleanExpression eqUnReadNotification() {
        return notification.isRead.eq(NotificationStatus.UNREAD);
    }

    private BooleanExpression gtNotificationTime(LocalDateTime lastNotificationTime) {
        return notification.createdDate.gt(lastNotificationTime);
    }
}
