package aws.retrospective.dto;

import aws.retrospective.entity.KudosTarget;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetKudosTargetResponseDto {

    private Long userId;
    private String username;
    private String thumbnail;

    public static GetKudosTargetResponseDto from(KudosTarget kudosTarget) {
        return new GetKudosTargetResponseDto(kudosTarget.getUser().getId(),
            kudosTarget.getUser().getUsername(), kudosTarget.getUser().getThumbnail());
    }

}
