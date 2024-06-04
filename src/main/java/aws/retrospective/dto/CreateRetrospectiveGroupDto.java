package aws.retrospective.dto;

import aws.retrospective.entity.ProjectStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateRetrospectiveGroupDto {

    @NotEmpty(message = "RetrospectiveGroup title is required.")
    private String title;

    @NotNull(message = "RetrospectiveGroup type is required.")
    private RetrospectiveType type;

    private ProjectStatus status = ProjectStatus.IN_PROGRESS;

    private UUID thumbnail;

    @NotEmpty(message = "RetrospectiveGroup description is required.")
    private String description;

}
