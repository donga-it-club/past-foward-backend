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

    @QueryProjection
    public GetTeamUsersResponseDto(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
