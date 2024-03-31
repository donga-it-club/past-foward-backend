package aws.retrospective.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetTeamUsersResponseDto {

    @Schema(description = "사용자 id", example = "1")
    private Long userId;

    @Schema(description = "사용자 이름", example = "hope")
    private String username;

    @Schema(description = "프로필 이미지 URL", example = "https://pf-uploaded-files.s3.ap-northeast-2.amazonaws.com/pingu.png")
    private String profileImageUrl;

    @QueryProjection
    public GetTeamUsersResponseDto(Long userId, String username, String profileImageUrl) {
        this.userId = userId;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }
}
