package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UpdateCommentRequestDto {

    @NotEmpty(message = "댓글 내용 수정하기 위한 내용이 필요합니다.")
    @Schema(description = "새로운 댓글 내용", example = "수고 많으십니다!")
    private String commentContent; // 수정할 내용
}
