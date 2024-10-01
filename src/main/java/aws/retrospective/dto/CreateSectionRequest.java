package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateSectionRequest {

    @NotNull(message = "회고카드가 작성된 회고보드 ID는 필수 값입니다.")
    @Schema(description = "화고카드가 작성될 회고보드 ID")
    private Long retrospectiveId;

    @NotNull(message = "회고카드가 작성된 템플릿 ID는 필수 값입니다.")
    @Schema(description = "회고카드가 작성된 템플릿")
    private Long templateSectionId;

    @NotEmpty(message = "회고카드에 작성된 내용은 필수 값입니다.")
    @Schema(description = "회고카드에 작성된 내용")
    private String sectionContent;

    @Builder
    private CreateSectionRequest(Long retrospectiveId, Long templateSectionId, String sectionContent) {
        this.retrospectiveId = retrospectiveId;
        this.templateSectionId = templateSectionId;
        this.sectionContent = sectionContent;
    }

}