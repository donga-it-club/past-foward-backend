package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IncreaseSectionLikesResponseDto {
    @Schema(description = "회고 카드 id", example = "1")
    private Long sectionId;
    @Schema(description = "회고 카드 좋아요 개수", example = "3")
    private long likeCnt;
}
