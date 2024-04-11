package aws.retrospective.repository;

import aws.retrospective.dto.GetTeamUsersResponseDto;
import java.util.List;

public interface UserTeamRepositoryCustom {

    List<GetTeamUsersResponseDto> findTeamMembers(Long teamId);
}
