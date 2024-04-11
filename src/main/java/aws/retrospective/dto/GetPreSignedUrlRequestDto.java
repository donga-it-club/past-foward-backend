package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GetPreSignedUrlRequestDto {

    @NotNull(message = "파일명은 필수 정보입니다.")
    @Schema(description = "파일명", example = "test.jpg")
    private String filename;
}
