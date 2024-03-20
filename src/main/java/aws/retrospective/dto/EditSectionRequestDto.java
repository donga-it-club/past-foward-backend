package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class EditSectionRequestDto {
    @Schema(description = "user id", example = "2")
    private Long userId; // 사용자
    @NotEmpty(message = "섹션을 수정하기 위한 내용이 필요합니다.")
    @Schema(description = "section content", example = "피드백 수용 - 내부 및 외부 피드백을 적극적으로 수용하여 제품을 개선하는 데 주저하지 않았다.")
    private String sectionContent; // 수정할 내용
}