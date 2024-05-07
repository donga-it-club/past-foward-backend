package aws.retrospective.dto;

import aws.retrospective.entity.Section;
import lombok.Getter;

@Getter
public class GetActionItemsResponseDto {

    private Long userId;
    private String username;
    private String thumbnail;

    private GetActionItemsResponseDto(Long userId, String username, String thumbnail) {
        this.userId = userId;
        this.username = username;
        this.thumbnail = thumbnail;
    }

    public static GetActionItemsResponseDto from(Section section) {
        return new GetActionItemsResponseDto(section.getUser().getId(),
            section.getUser().getUsername(), section.getUser().getThumbnail());
    }
}
