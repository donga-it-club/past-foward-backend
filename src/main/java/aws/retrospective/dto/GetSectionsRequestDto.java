package aws.retrospective.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GetSectionsRequestDto {

    @NotNull(message = "회고보드 id는 필수 값입니다.")
    private Long retrospectiveId;

    @NotNull(message = "팀 id는 필수 값입니다.")
    private Long teamId;
}
