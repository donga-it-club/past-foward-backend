package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeleteCommentRequestDto {

    @NotNull(message = "사용자 id는 필수 정보입니다.")
    @Schema(description = "user id", example = "1")
    private Long userId;
}
