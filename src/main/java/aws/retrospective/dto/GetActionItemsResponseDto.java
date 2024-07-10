package aws.retrospective.dto;

import aws.retrospective.entity.ActionItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetActionItemsResponseDto {

    private Long userId;
    private String username;
    private String thumbnail;

    public static GetActionItemsResponseDto from(ActionItem actionItem) {
        return new GetActionItemsResponseDto(actionItem.getUser().getId(),
            actionItem.getUser().getUsername(), actionItem.getUser().getThumbnail());
    }
}