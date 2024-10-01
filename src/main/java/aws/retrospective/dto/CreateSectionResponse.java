package aws.retrospective.dto;

import aws.retrospective.entity.Section;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateSectionResponse {

    @Schema(description = "회고 카드 id", example = "1")
    private Long id;
    @Schema(description = "user id", example = "1")
    private Long userId;
    @Schema(description = "retrospective id", example = "2")
    private Long retrospectiveId;
    @Schema(description = "회고 카드 내용", example = "프로젝트 관리 - 일정 관리와 작업 분배가 효과적으로 이루어졌다.")
    private String sectionContent;

    @Builder
    private CreateSectionResponse(Long id, Long userId, Long retrospectiveId,
        String sectionContent) {
        this.id = id; // 회고 카드 ID
        this.userId = userId; // 회고 카드를 작성한 사용자 ID
        this.retrospectiveId = retrospectiveId; // 회고 카드가 작성된 회고 보드 ID
        this.sectionContent = sectionContent; // 회고 카드의 내용
    }

    public static CreateSectionResponse of(Section section) {
        return CreateSectionResponse.builder()
            .id(section.getId())
            .userId(section.getUser().getId())
            .retrospectiveId(section.getRetrospective().getId())
            .sectionContent(section.getContent())
            .build();
    }
}
