package aws.retrospective.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IncreaseSectionLikesResponseDto {

    private Long sectionId;
    private long likeCnt;
}
