package aws.retrospective.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GetCommentsResponseDto {

    @Schema(description = "comment id", example = "1")
    private Long commentId;

    @Schema(description = "user name", example = "llcodingll")
    private String username;

    @Schema(description = "댓글 내용", example = "커피 마시고 싶어요... 제발.")
    private String content;

    @Schema(description = "댓글 등록일", example = "2021-07-01T00:00:00")
    private LocalDateTime createdDate;

    @QueryProjection
    public GetCommentsResponseDto(Long commentId, String username, String content,
        LocalDateTime createdDate) {
        this.commentId = commentId;
        this.username = username;
        this.content = content;
        this.createdDate = createdDate;
    }

}