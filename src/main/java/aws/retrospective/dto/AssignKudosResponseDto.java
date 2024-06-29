package aws.retrospective.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AssignKudosResponseDto {

    private Long kudosId;
    private Long sectionId;
    private Long userId;

    @Builder
    public AssignKudosResponseDto(Long kudosId, Long sectionId, Long userId) {
        this.kudosId = kudosId;
        this.sectionId = sectionId;
        this.userId = userId;
    }
}
