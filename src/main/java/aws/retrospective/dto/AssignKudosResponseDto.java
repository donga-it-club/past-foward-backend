package aws.retrospective.dto;

import aws.retrospective.entity.KudosTarget;
import lombok.Getter;

@Getter
public class AssignKudosResponseDto {

    private Long kudosId;
    private Long sectionId;
    private Long userId;

    private AssignKudosResponseDto(Long kudosId, Long sectionId, Long userId) {
        this.kudosId = kudosId;
        this.sectionId = sectionId;
        this.userId = userId;
    }

    public static AssignKudosResponseDto convertResponse(KudosTarget kudosTarget) {
        return new AssignKudosResponseDto(kudosTarget.getId(), kudosTarget.getSection().getId(),
            kudosTarget.getUser().getId());
    }
}
