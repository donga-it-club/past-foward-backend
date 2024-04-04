package aws.retrospective.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Getter // Lombok을 사용하여 getter 메서드 자동 생성
@ToString
@EqualsAndHashCode
public class InviteTeamMemberDTO {
    private String invitationCode; // 초대 코드
    private String invitationUrl; // 초대 링크
    private LocalDateTime expirationTime; // 초대 만료 시간
    private byte[] qrCodeImage; // QR 코드 이미지

    // 생성자 추가
    public InviteTeamMemberDTO(String invitationCode, String invitationUrl, LocalDateTime expirationTime, byte[] qrCodeImage) {
        this.invitationCode = invitationCode;
        this.invitationUrl = invitationUrl;
        this.expirationTime = expirationTime;
        this.qrCodeImage = qrCodeImage;
    }
}
