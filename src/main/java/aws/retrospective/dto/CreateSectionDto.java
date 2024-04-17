package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CreateSectionDto {

    @Schema(description = "retrospective id", example = "2")
    private Long retrospectiveId;
    @Schema(description = "template_section id", example = "3")
    private Long templateSectionId;

    @NotEmpty(message = "회고 카드의 내용은 필수 입력 값입니다.")
    @Schema(description = "회고 카드의 내용", example = "프로젝트 관리 - 일정 관리와 작업 분배가 효과적으로 이루어졌다.")
    private String sectionContent;
}