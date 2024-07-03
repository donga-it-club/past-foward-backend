package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EditSectionResponseDto {

    @Schema(description = "회고 카드 id", example = "1")
    private Long sectionId; // 섹션 id
    @Schema(description = "수정된 회고 카드 내용", example = "피드백 수용 - 내부 및 외부 피드백을 적극적으로 수용하여 제품을 개선하는 데 주저하지 않았다.")
    private String content; // 수정된 섹션 내용

    @Builder
    public EditSectionResponseDto(Long sectionId, String content) {
        this.sectionId = sectionId; // 회고 카드 ID
        this.content = content; // 회고 카드 내용
    }
}
