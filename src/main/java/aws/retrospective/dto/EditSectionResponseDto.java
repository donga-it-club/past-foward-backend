package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditSectionResponseDto {
    @Schema(description = "section id", example = "1")
    private Long sectionId; // 섹션 id
    @Schema(description = "section content", example = "피드백 수용 - 내부 및 외부 피드백을 적극적으로 수용하여 제품을 개선하는 데 주저하지 않았다.")
    private String content; // 수정된 섹션 내용
}
