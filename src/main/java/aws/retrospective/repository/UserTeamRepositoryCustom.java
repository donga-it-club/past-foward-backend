package aws.retrospective.repository;

import aws.retrospective.dto.GetTeamMembersResponseDto;
import java.util.List;

public interface UserTeamRepositoryCustom {

    List<GetTeamMembersResponseDto> findTeamMembers(Long teamId);
}
