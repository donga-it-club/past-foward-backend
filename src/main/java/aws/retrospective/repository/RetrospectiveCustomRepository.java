package aws.retrospective.repository;

import aws.retrospective.dto.GetRetrospectivesDto;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.User;
import java.util.List;

public interface RetrospectiveCustomRepository {

    List<Retrospective> findRetrospectives(User user, GetRetrospectivesDto dto);

    long countRetrospectives(User user, GetRetrospectivesDto dto);
}
