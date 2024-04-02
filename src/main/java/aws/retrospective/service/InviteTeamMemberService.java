package aws.retrospective.service;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.InviteTeamMemberDTO;
import aws.retrospective.entity.Team;
import aws.retrospective.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteTeamMemberService {

    private final TeamRepository teamRepository;
    private final String domainUrl;

    private static final int EXPIRATION_HOURS = 2;

    public CommonApiResponse<InviteTeamMemberDTO> generateInvitation(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found for id: " + teamId));

        String invitationCode = UUID.randomUUID().toString();
        String invitationUrl = domainUrl + "/invitations/" + invitationCode;
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(EXPIRATION_HOURS);

        InviteTeamMemberDTO inviteTeamMemberDTO = new InviteTeamMemberDTO(invitationCode, invitationUrl, expirationTime);

        return CommonApiResponse.successResponse(HttpStatus.OK, inviteTeamMemberDTO);
    }

    // 초대 토큰 유효성을 검증하는 메서드
    private void validateInvitation(String invitationCode) {
        // 초대 토큰 유효성 검증 로직 추가
        // 만료 시간 확인 및 팀 정보 반환
        // 예시로 항상 유효하다고 가정
        boolean isValid = true; // 유효성 검증 결과
        if (!isValid) {
            throw new IllegalArgumentException("Invalid invitation code: " + invitationCode);
        }
    }
}