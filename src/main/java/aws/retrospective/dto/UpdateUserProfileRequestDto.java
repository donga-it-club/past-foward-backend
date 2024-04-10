package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateUserProfileRequestDto {

    @NotNull(message = "썸네일은 필수 정보입니다.")
    @Schema(description = "썸네일", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String thumbnail;
}
