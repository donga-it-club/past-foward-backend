package aws.retrospective.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetTeamUsersResponseDto {

    @Schema(description = "사용자 id", example = "1")
    private Long userId;

    @Schema(description = "사용자 이름", example = "hope")
    private String username;

    @Schema(description = "프로필 이미지", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String profileImage;

    @Schema(description = "이메일", example = "test@gmail.com")
    private String email;

    @Schema(description = "참여 일자", example = "2021-07-01T00:00:00")
    private LocalDateTime joinedAt;

    @QueryProjection
    public GetTeamUsersResponseDto(Long userId, String username, String profileImageUrl, String email, LocalDateTime joinedAt) {
        this.userId = userId;
        this.username = username;
        this.profileImage = profileImageUrl;
        this.email = email;
        this.joinedAt = joinedAt;
    }
}
