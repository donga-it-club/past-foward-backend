package aws.retrospective.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InviteTeamMemberDTO {
    private String invitationCode; // 초대 코드
    private String invitationUrl; // 초대 링크
    private LocalDateTime expirationTime; // 초대 만료 시간
}
