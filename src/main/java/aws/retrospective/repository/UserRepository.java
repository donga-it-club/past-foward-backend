package aws.retrospective.repository;

import aws.retrospective.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


    User findByUsername(String username);

    Optional<User> findByTenantId(String tenantId);
}
