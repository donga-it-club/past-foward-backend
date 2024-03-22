package aws.retrospective.dto;

import aws.retrospective.entity.ProjectStatus;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateRetrospectiveResponseDto {

    private Long id;
    private String title;
    private Long teamId;
    private Long userId;
    private Long templateId;
    private ProjectStatus status;
    private UUID thumbnail;
}
