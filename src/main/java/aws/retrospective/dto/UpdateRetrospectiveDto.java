package aws.retrospective.dto;

import aws.retrospective.entity.ProjectStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateRetrospectiveDto {

    @NotEmpty(message = "Retrospective title is required")
    private String title;

    private Long teamId;

    @NotNull(message = "User id is required")
    private Long userId;

    @NotEmpty(message = "Retrospective description is required")
    private String description;

    @NotNull(message = "Retrospective status is required")
    private ProjectStatus status = ProjectStatus.IN_PROGRESS;

    private UUID thumbnail;

}
