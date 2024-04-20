package aws.retrospective.repository;

import aws.retrospective.entity.UserTeam;
import aws.retrospective.entity.UserTeamRole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long>,
    UserTeamRepositoryCustom {

    public void deleteByTeamIdAndUserId(Long teamId, Long userId);

    public Optional<UserTeam> findByTeamIdAndRole(Long teamId, UserTeamRole role);

}
