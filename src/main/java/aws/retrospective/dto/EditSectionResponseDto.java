package aws.retrospective.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditSectionResponseDto {

    private Long sectionId; // 섹션 id
    private String content; // 수정된 섹션 내용
}
