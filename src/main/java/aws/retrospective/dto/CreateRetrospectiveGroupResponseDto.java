package aws.retrospective.dto;

import aws.retrospective.entity.ProjectStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class CreateRetrospectiveGroupResponseDto {

    private Long id;
    private String title;
    private Long userId;
    private ProjectStatus status;
    private UUID thumbnail;
    private String description;

}
