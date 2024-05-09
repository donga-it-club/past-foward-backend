package aws.retrospective.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AssignKudosRequestDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long sectionId;
}
