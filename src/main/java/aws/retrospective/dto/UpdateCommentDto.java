package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCommentDto {

    @NotNull(message = "CommentId is required")
    private Long commentId; // 댓글 ID

    @NotNull(message = "User id is required")
    @Schema(description = "user id", example = "1")
    private Long userId; // 수정을 시도한 사용자의 ID

    @NotNull(message = "Section id is required")
    @Schema(description = "comment_section id", example = "2")
    private Long commentSectionId; // 수정을 시도한 섹션의 ID

    @NotEmpty(message = "Comment content is required")
    @Schema(description = "댓글 내용", example = "수정된 댓글입니다.")
    private String updatedContent; // 수정된 댓글 내용


    public UpdateCommentDto(Long commentId, Long userId, Long commentSectionId, String updatedContent) {
        this.commentId = commentId;
        this.userId = userId;
        this.commentSectionId = commentSectionId;
        this.updatedContent = updatedContent;
    }
}