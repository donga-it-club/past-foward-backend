package aws.retrospective.dto;

import aws.retrospective.entity.ProjectStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateRetrospectiveGroupDto {

    @NotEmpty(message = "Retrospective Group title is required")
    private String title;

    @NotEmpty(message = "Retrospective Group description is required")
    private String description;

    @NotNull(message = "Retrospective Group status is required")
    private ProjectStatus status = ProjectStatus.IN_PROGRESS;

    private UUID thumbnail;
}