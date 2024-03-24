package aws.retrospective.service;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.InviteTeamMemberDTO;
import aws.retrospective.entity.Team;
import aws.retrospective.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteTeamMemberService {

    private final TeamRepository teamRepository;

    @Value("${domain.url}")
    private String domainUrl;

    public CommonApiResponse<InviteTeamMemberDTO> generateInvitation(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found for id: " + teamId));

        String teamIdString = String.valueOf(teamId);
        String invitationCode = UUID.randomUUID().toString() + "-" + teamIdString;
        String invitationUrl = domainUrl + "/invitations/" + invitationCode;
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(2);

        InviteTeamMemberDTO inviteTeamMemberDTO = new InviteTeamMemberDTO();
        inviteTeamMemberDTO.setInvitationCode(invitationCode);
        inviteTeamMemberDTO.setInvitationUrl(invitationUrl);
        inviteTeamMemberDTO.setExpirationTime(expirationTime);

        return CommonApiResponse.successResponse(HttpStatus.OK, inviteTeamMemberDTO);
    }

    public void generateInvitationQR(String invitationUrl) {
        // 여기에 초대 링크를 기반으로 QR 코드 생성하는 로직 추가
    }
}
