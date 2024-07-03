package aws.retrospective.dto;

import aws.retrospective.entity.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateRetrospectiveGroupDto {

    @NotEmpty(message = "RetrospectiveGroup title is required")
    @Schema(description = "회고 그룹 제목", example = "Past Forward 회고")
    private String title;

    private ProjectStatus status = ProjectStatus.IN_PROGRESS;

    private UUID thumbnail;

    @NotEmpty(message = "RetrospectiveGroup description is required.")
    @Schema(description = "회고 그룹 설명", example = "Past Forward 회고 1주차~15주차 모음")
    private String description;

}