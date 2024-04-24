package aws.retrospective.dto;

import aws.retrospective.entity.UserTeamRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AcceptInviteResponseDto {

    private Long teamId;
    private Long userId;
    private UserTeamRole role;

}
