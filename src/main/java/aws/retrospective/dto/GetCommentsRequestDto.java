package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCommentsRequestDto {

    @Schema(description = "섹션 id", example = "1")
    @NotNull(message = "섹션 id는 필수 값입니다.")
    private Long sectionId;

}
