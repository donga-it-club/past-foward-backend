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
public class CreateRetrospectiveDto {

    @NotEmpty(message = "Retrospective title is required.")
    private String title;

    private RetrospectiveType type;

    //TODO: 추후 Cognito 이용해서 request body에서 받지 않고 perssitence layer에서 받아오도록 수정
    private Long userId;

    @NotNull(message = "Template id is required.")
    private Long templateId;

    private ProjectStatus status = ProjectStatus.IN_PROGRESS;

    private UUID thumbnail;

    private LocalDateTime startDate;

    @NotEmpty(message = "Retrospective description is required.")
    private String description;
}
