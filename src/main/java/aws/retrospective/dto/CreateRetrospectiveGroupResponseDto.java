package aws.retrospective.dto;

import aws.retrospective.entity.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateRetrospectiveGroupResponseDto {

    @Schema(description = "회고 그룹 id", example = "1")
    private Long id;

    @Schema(description = "회고 그룹 제목", example = "Past Forward 회고")
    private String title;

    @Schema(description = "user id", example = "1")
    private Long userId;

    @Schema(description = "회고 그룹 설명", example = "Past Forward 전체 회고 모음")
    private String description;

    private ProjectStatus status;

    private UUID thumbnail;
}