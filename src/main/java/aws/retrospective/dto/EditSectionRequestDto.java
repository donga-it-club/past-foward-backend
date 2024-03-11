package aws.retrospective.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class EditSectionRequestDto {

    private Long userId; // 사용자
    @NotEmpty(message = "섹션을 수정하기 위한 내용이 필요합니다.")
    private String sectionContent; // 수정할 내용
}