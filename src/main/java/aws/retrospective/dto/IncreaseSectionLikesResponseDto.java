package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class IncreaseSectionLikesResponseDto {

    @Schema(description = "회고 카드 id", example = "1")
    private Long sectionId;
    @Schema(description = "회고 카드 좋아요 개수", example = "3")
    private long likeCnt;

    private IncreaseSectionLikesResponseDto(Long sectionId, long likeCnt) {
        this.sectionId = sectionId;
        this.likeCnt = likeCnt;
    }

    public static IncreaseSectionLikesResponseDto of(Long sectionId, long likeCnt) {
        return new IncreaseSectionLikesResponseDto(sectionId, likeCnt);
    }
}
