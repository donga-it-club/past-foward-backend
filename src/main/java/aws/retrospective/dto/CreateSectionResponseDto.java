package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateSectionResponseDto {
    @Schema(description = "회고 카드 id", example = "1")
    private Long id;
    @Schema(description = "user id", example = "1")
    private Long userId;
    @Schema(description = "retrospective id", example = "2")
    private Long retrospectiveId;
    @Schema(description = "회고 카드 내용", example = "프로젝트 관리 - 일정 관리와 작업 분배가 효과적으로 이루어졌다.")
    private String sectionContent;
}
