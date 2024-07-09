package aws.retrospective.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetActionItemsResponseDto {

    private Long userId;
    private String username;
    private String thumbnail;
}
