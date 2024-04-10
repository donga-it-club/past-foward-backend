package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserProfileResponseDto {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;
    @Schema(description = "사용자 이메일", example = "test@gmail.com")
    private String email;
    @Schema(description = "사용자 썸네일", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String thumbnail;
}
