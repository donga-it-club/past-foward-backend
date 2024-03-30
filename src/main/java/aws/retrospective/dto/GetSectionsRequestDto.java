package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GetSectionsRequestDto {

    @Schema(description = "회고보드 id", example = "1")
    @NotNull(message = "회고보드 id는 필수 값입니다.")
    private Long retrospectiveId;

    @Schema(description = "팀 id", example = "1")
    @NotNull(message = "팀 id는 필수 값입니다.")
    private Long teamId;
}
