package aws.retrospective.dto;

import lombok.Data;

@Data
public class EditSectionRequestDto {

    private Long userId; // 사용자
    private String sectionContent; // 수정할 내용
}