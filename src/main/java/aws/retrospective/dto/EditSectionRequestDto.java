package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class EditSectionRequestDto {

    @NotEmpty
    @Schema(description = "회고 수정을 시도하는 사용자", example = "1")
    private Long userId;

    @NotEmpty(message = "회고 카드의 내용을 수정하기 위한 내용이 필요합니다.")
    @Schema(description = "새로운 회고 카드 내용", example = "피드백 수용 - 내부 및 외부 피드백을 적극적으로 수용하여 제품을 개선하는 데 주저하지 않았다.")
    private String sectionContent; // 수정할 내용
}