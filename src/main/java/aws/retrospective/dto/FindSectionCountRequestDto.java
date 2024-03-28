package aws.retrospective.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FindSectionCountRequestDto {

    @NotNull(message = "회고 보드 ID는 필수 입력 값입니다.")
    private Long retrospectiveId;

    @NotNull(message = "템플릿 섹션 ID는 필수 입력 값입니다.")
    private Long templateSectionId;
}
