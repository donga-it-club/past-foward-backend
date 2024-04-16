package aws.retrospective.repository;

import aws.retrospective.entity.TeamInvite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamInvitationRepository extends JpaRepository<TeamInvite,Long> {
    Optional<Object> findByInvitationCode(String invitationCode);
}
