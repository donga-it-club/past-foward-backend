package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class GetSectionsRequestDto {

    @Schema(description = "회고보드 id", example = "1")
    @NotNull(message = "회고보드 id는 필수 값입니다.")
    private Long retrospectiveId; // Long타입

    @Schema(description = "팀 id", example = "1")
    private Long teamId;

    @Builder
    private GetSectionsRequestDto(Long retrospectiveId, Long teamId) {
        this.retrospectiveId = retrospectiveId;
        this.teamId = teamId;
    }

}
