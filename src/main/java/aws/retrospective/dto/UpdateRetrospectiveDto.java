package aws.retrospective.dto;

import aws.retrospective.entity.ProjectStatus;
import jakarta.validation.constraints.NotEmpty;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateRetrospectiveDto {

    @NotEmpty(message = "Retrospective title is required")
    private String title;

    private Long teamId;

    @NotEmpty(message = "User id is required")
    private Long userId;

    @NotEmpty(message = "Retrospective description is required")
    private String description;

    @NotEmpty(message = "Retrospective status is required")
    private ProjectStatus status = ProjectStatus.IN_PROGRESS;

    private UUID thumbnail;

}
