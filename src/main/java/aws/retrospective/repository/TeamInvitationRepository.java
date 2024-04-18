package aws.retrospective.repository;

import aws.retrospective.entity.TeamInvite;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamInvitationRepository extends JpaRepository<TeamInvite, Long> {

    Optional<TeamInvite> findByInvitationCode(String invitationCode);
}
