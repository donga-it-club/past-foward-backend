package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CreateSectionDto {
    @Schema(description = "user id", example = "1")
    private Long userId;
    @Schema(description = "retrospective id", example = "2")
    private Long retrospectiveId;
    @Schema(description = "template_section id", example = "3")
    private Long templateSectionId;

    @NotEmpty(message = "Section content is required")
    @Schema(description = "section content", example = "프로젝트 관리 - 일정 관리와 작업 분배가 효과적으로 이루어졌다.")
    private String sectionContent;
}