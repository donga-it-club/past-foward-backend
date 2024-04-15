package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignUserRequestDto {

    @NotNull
    @Schema(description = "팀 ID")
    private Long teamId;

    @NotNull
    @Schema(description = "사용자 ID")
    private Long userId;

    @NotNull
    @Schema(description = "회고 보드 ID")
    private Long retrospectiveId;

    @NotNull
    @Schema(description = "회고 카드 ID")
    private Long sectionId;
}
