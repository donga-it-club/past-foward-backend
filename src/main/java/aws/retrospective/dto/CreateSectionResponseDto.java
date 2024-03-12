package aws.retrospective.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateSectionResponseDto {

    private Long id;
    private Long userId;
    private Long retrospectiveId;
    private String sectionContent;
}
