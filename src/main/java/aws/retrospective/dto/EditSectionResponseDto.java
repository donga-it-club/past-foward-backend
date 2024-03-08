package aws.retrospective.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditSectionResponseDto {

    Long sectionId; // 섹션 id
    String content; // 수정된 섹션 내용
}
