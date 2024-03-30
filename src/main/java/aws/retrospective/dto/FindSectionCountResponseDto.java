package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FindSectionCountResponseDto {

    @Schema(description = "섹션에 등록된 카드 개수", example = "1")
    private int count;
}
