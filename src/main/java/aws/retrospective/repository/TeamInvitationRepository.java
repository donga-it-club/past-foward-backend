package aws.retrospective.repository;

import aws.retrospective.entity.TeamInvite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamInvitationRepository extends JpaRepository<TeamInvite,Long> {
}
