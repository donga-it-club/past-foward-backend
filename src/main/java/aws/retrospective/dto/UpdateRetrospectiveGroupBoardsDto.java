package aws.retrospective.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateRetrospectiveGroupBoardsDto {

    @NotNull(message = "existed retrospective id is required")
    private List<Long> retrospectiveIds;
}

