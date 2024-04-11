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

    @Schema(description = "프로필 이미지", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String profileImage;

    @QueryProjection
    public GetTeamUsersResponseDto(Long userId, String username, String profileImageUrl) {
        this.userId = userId;
        this.username = username;
        this.profileImage = profileImageUrl;
    }
}
