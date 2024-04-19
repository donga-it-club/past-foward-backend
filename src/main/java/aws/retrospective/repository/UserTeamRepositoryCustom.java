package aws.retrospective.repository;

import aws.retrospective.dto.GetTeamUsersResponseDto;
import aws.retrospective.entity.UserTeam;
import java.util.List;
import java.util.Optional;

public interface UserTeamRepositoryCustom {

    List<GetTeamUsersResponseDto> findTeamMembers(Long teamId);
    Optional<UserTeam> findByUserIdAndTeamId(Long userId, Long teamId);
}