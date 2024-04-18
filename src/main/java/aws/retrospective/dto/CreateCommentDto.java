package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCommentDto {

    @Schema(description = "section_comment id", example = "3")
    private Long sectionCommentId;
    @Schema(description = "section id", example = "2")
    private Long sectionId;
    @NotEmpty(message = "댓글 내용은 필수 입력 값입니다.")
    @Schema(description = "댓글 내용", example = "여러분 프로젝트하느라 수고 많으셨습니다.")
    private String commentContent;
}
