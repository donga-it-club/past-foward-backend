package aws.retrospective.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("NoticeBoardViewCounting")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeBoardViewCounting {

    @Id
    private Long postId; // key
    @Getter
    private Integer viewCount; // value

    public static NoticeBoardViewCounting of(Long postId, Integer viewCount) {
        return new NoticeBoardViewCounting(postId, viewCount);
    }

    public void incrementViewCount() {
        this.viewCount++;
    }
}

