package aws.retrospective.dto;

import aws.retrospective.entity.ProjectStatus;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateRetrospectiveDto {

    String title;

    Long teamId;

    Long userId;

    String description;

    ProjectStatus status = ProjectStatus.IN_PROGRESS;

    UUID thumbnail;

}
