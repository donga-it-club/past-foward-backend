package aws.retrospective.dto;

import aws.retrospective.entity.Section;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CreateSectionResponseDto {

    @Schema(description = "회고 카드 id", example = "1")
    private Long sectionId;
    @Schema(description = "user id", example = "1")
    private Long userId;
    @Schema(description = "retrospective id", example = "2")
    private Long retrospectiveId;
    @Schema(description = "회고 카드 내용", example = "프로젝트 관리 - 일정 관리와 작업 분배가 효과적으로 이루어졌다.")
    private String sectionContent;

    private CreateSectionResponseDto(Long sectionId, Long userId, Long retrospectiveId,
        String sectionContent) {
        this.sectionId = sectionId;
        this.userId = userId;
        this.retrospectiveId = retrospectiveId;
        this.sectionContent = sectionContent;
    }

    public static CreateSectionResponseDto of(Long userId, Long retrospectiveId, Section section) {
        return new CreateSectionResponseDto(section.getId(), userId, retrospectiveId,
            section.getContent());
    }
}
