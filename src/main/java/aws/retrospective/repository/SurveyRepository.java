package aws.retrospective.repository;

import aws.retrospective.entity.Survey;
import aws.retrospective.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    boolean existsByUser(User user);


}