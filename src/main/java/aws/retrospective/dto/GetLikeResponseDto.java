package aws.retrospective.dto;

import aws.retrospective.entity.Likes;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetLikeResponseDto {

    @Schema(description = "좋아요 누른 사용자 이름", example = "김감자")
    private String username;
    @Schema(description = "좋아요 누른 시간", example = "2024-05-10T00:00:00")
    private LocalDateTime createdDate;

    public static GetLikeResponseDto of(Likes likes) {
        return new GetLikeResponseDto(likes.getUser().getUsername(), likes.getCreatedDate());
    }
}
