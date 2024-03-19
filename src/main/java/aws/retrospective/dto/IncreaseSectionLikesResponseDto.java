package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IncreaseSectionLikesResponseDto {
    @Schema(description = "section id", example = "1")
    private Long sectionId;
    @Schema(description = "like count", example = "3")
    private long likeCnt;
}
