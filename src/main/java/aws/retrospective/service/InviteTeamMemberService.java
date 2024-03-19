package aws.retrospective.service;


import aws.retrospective.dto.InviteTeamMemberDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InviteTeamMemberService {

    public InviteTeamMemberDTO generateInvitation(String teamId) {
        // 랜덤한 초대 코드 생성 (여기서는 UUID 사용)
        String invitationCode = UUID.randomUUID().toString();

        // 초대 링크 생성
        String invitationUrl = "http://example.com/invitations/" + invitationCode;

        // 초대 만료 시간 설정 (현재 시간으로부터 2시간 뒤)
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(2);

        // 초대 정보를 담은 DTO 객체 반환
        InviteTeamMemberDTO inviteTeamMemberDTO = new InviteTeamMemberDTO();
        inviteTeamMemberDTO.setInvitationCode(invitationCode);
        inviteTeamMemberDTO.setInvitationUrl(invitationUrl);
        inviteTeamMemberDTO.setExpirationTime(expirationTime);

        return inviteTeamMemberDTO;
    }

    public void generateInvitationQR(String invitationUrl) {
        // 여기에 초대 링크를 기반으로 QR 코드 생성하는 로직 추가
    }
}
