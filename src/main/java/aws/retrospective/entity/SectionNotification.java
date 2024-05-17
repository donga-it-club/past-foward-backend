package aws.retrospective.entity;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("sectionNotification")
@Getter
public class SectionNotification {

    @Id
    private String id;
    private String lastCommentTimeAt;
    private String lastLikeTimeAt;

    @Builder
    public SectionNotification(String id, String lastCommentTimeAt, String lastLikeTimeAt) {
        this.id = id;
        this.lastCommentTimeAt = lastCommentTimeAt;
        this.lastLikeTimeAt = lastLikeTimeAt;
    }

    public void updateLastCommentTimeAt(LocalDateTime time) {
        this.lastCommentTimeAt = time.toString();
    }

    public void updateLastLikeTimeAt(LocalDateTime time) {
        this.lastLikeTimeAt = time.toString();
    }
}
