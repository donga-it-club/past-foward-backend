package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateCommentResponseDto {

    @Schema(description = "댓글 id", example = "1")
    private Long commentId; // 댓글 id
    @Schema(description = "수정된 댓글 내용", example = "수고 많으십니다!")
    private String content; // 수정된 댓글 내용
}
