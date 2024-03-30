package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FindSectionCountRequestDto {

    @Schema(description = "회고보드 ID", example = "1")
    @NotNull(message = "회고보드 ID는 필수 입력 값입니다.")
    private Long retrospectiveId;

    @Schema(description = "섹션 ID", example = "1")
    @NotNull(message = "섹션 ID는 필수 입력 값입니다.")
    private Long templateSectionId;
}
