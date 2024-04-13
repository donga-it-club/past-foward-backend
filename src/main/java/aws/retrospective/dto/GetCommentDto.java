package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentDto {

    @Schema(description = "댓글 id", example = "1")
    private Long commentId;
    @Schema(description = "댓글 내용", example = "매우 공감됩니다!")
    private String content;
    @Schema(description = "댓글 작성자", example = "hope")
    private String username;
}
