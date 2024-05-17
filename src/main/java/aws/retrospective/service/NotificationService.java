package aws.retrospective.service;

import aws.retrospective.dto.SectionNotificationDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.entity.Likes;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.SectionNotification;
import aws.retrospective.repository.CommentRepository;
import aws.retrospective.repository.LikesRepository;
import aws.retrospective.repository.SectionNotificationRepository;
import aws.retrospective.repository.SectionRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SectionRepository sectionRepository;
    private final SectionNotificationRepository notificationRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;

    private final String SECTION_ID = "sectionId_";

    @Transactional
    public List<SectionNotificationDto> getNewCommentsAndLikes() {
        // 1. 모든 Section을 조회한다.
        List<Section> sections = sectionRepository.findAll();
        List<SectionNotificationDto> notifications = new ArrayList<>();

        // 2. 모든 Section 순회한다.
        sections
            .forEach(section -> {
                // DB에서 마지막으로 작성된 댓글과 좋아요를 조회한다.
                Comment lastCommentInDB = getLatestCommentBySection(section);
                Likes lastLikeInDB = getLatestLikeBySection(section);
                LocalDateTime lastCommentTimeInDB = getLastTimeInDB(lastCommentInDB);
                LocalDateTime lastLikeTimeInDB = getLastTimeInDB(lastLikeInDB);

                // Redis에서 마지막으로 알림이 전송된 시간을 조회한다.
                SectionNotification findPrevNotification = getLastTimeInRedis(
                    SECTION_ID + section.getId());

                // 마지막으로 작성된 댓글과 좋아요 시간을 비교하여 새로운 댓글과 좋아요를 조회한다.
                List<Comment> comments = getComments(section.getId(), findPrevNotification);
                List<Likes> likes = getLikes(section.getId(), findPrevNotification);

                // 마지막으로 작성된 댓글과 좋아요 시간을 Redis에 저장한다.
                updateRedisValue(findPrevNotification, section.getId(), lastCommentTimeInDB,
                    lastLikeTimeInDB);

                // 새로운 댓글과 좋아요가 있을 경우 알림을 생성한다.
                if (!likes.isEmpty() || !comments.isEmpty()) {
                    notifications.add(createNotification(section, comments, likes));
                }
            });

        return notifications;
    }

    private void updateRedisValue(SectionNotification notification, Long sectionId,
        LocalDateTime lastCommentTimeInDB,
        LocalDateTime lastLikeTimeInDB) {
        if (notification == null) {
            notification = SectionNotification.builder()
                .id(SECTION_ID + sectionId)
                .lastCommentTimeAt(
                    lastCommentTimeInDB != null ? lastCommentTimeInDB.toString()
                        : LocalDateTime.now(ZoneOffset.UTC).toString())
                .lastLikeTimeAt(lastLikeTimeInDB != null ? lastLikeTimeInDB.toString()
                    : LocalDateTime.now(ZoneOffset.UTC).toString())
                .build();
            notificationRepository.save(notification);
        } else {
            if (lastCommentTimeInDB != null) {
                notification.updateLastCommentTimeAt(lastCommentTimeInDB);
            }
            if (lastLikeTimeInDB != null) {
                notification.updateLastLikeTimeAt(lastLikeTimeInDB);
            }
            notificationRepository.save(notification);
        }
    }

    private LocalDateTime getLastTimeInDB(Object entity) {
        if (entity != null) {
            if (entity instanceof Comment) {
                return ((Comment) entity).getCreatedDate();
            } else if (entity instanceof Likes) {
                return ((Likes) entity).getCreatedDate();
            }
        }
        return null;
    }

    private SectionNotification getLastTimeInRedis(String key) {
        return notificationRepository.findById(key).orElse(null);
    }

    private Likes getLatestLikeBySection(Section section) {
        return likesRepository.findLatestLikeBySection(section.getId());
    }

    private Comment getLatestCommentBySection(Section section) {
        return commentRepository.findLatestCommentBySection(section.getId());
    }

    private List<Comment> getComments(Long sectionId, SectionNotification notification) {
        if (notification != null) {
            return commentRepository.findCommentsAfterDate(sectionId,
                convertStringToLocalDateTime(notification.getLastCommentTimeAt()));
        } else {
            return commentRepository.findAllComments(sectionId);
        }
    }

    private List<Likes> getLikes(Long sectionId, SectionNotification notification) {
        if (notification != null) {
            return likesRepository.findLikesAfterDate(sectionId,
                convertStringToLocalDateTime(notification.getLastLikeTimeAt()));
        } else {
            return likesRepository.findAllLikes(sectionId);
        }
    }

    private SectionNotificationDto createNotification(Section section, List<Comment> comments,
        List<Likes> likes) {
        return SectionNotificationDto.createNotification(section, comments, likes);
    }

    private static LocalDateTime convertStringToLocalDateTime(String time) {
        return LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
