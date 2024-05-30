package aws.retrospective.dto;

import aws.retrospective.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GetCommentDto {

    @Schema(description = "댓글 id", example = "1")
    private Long commentId;
    @Schema(description = "작성자 id", example = "1")
    private Long userId;
    @Schema(description = "댓글 내용", example = "매우 공감됩니다!")
    private String content;
    @Schema(description = "댓글 작성자", example = "hope")
    private String username;
    @Schema(description = "프로필 이미지", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String thumbnail;

    public static GetCommentDto from(Comment comment) {
        return new GetCommentDto(comment.getId(), comment.getUser().getId(), comment.getContent(),
            comment.getUser().getUsername(), comment.getUser().getThumbnail());
    }
}
