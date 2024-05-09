package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AssignKudosRequestDto {

    @NotNull
    @Schema(description = "칭찬할 사용자의 ID", example = "1")
    private Long userId;
}
