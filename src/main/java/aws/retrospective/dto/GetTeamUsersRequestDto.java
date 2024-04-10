package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GetTeamUsersRequestDto {

    @Schema(description = "회고 보드 id", example = "1")
    @NotNull(message = "회고 보드 id는 필수 입력 값입니다.")
    private Long retrospectiveId;;
}
