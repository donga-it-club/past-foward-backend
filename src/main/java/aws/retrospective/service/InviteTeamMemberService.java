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

    private final TeamRepository teamRepository; // 팀 정보를 조회하는 데 사용되는 리포지토리

    @Value("${domain.url}")
    private String domainUrl; // 초대 링크 생성 시 사용되는 도메인 URL

    // 초대 토큰의 만료 시간 (2시간)
    private static final int EXPIRATION_HOURS = 2;

    public CommonApiResponse<InviteTeamMemberDTO> generateInvitation(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found for id: " + teamId));

        // 초대 토큰 생성
        String invitationCode = UUID.randomUUID().toString(); // 랜덤한 UUID 생성
        String invitationUrl = domainUrl + "/invitations/" + invitationCode; // 초대 링크 URL 생성
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(EXPIRATION_HOURS); // 만료 시간 설정 (현재 시간 + 2시간)

        InviteTeamMemberDTO inviteTeamMemberDTO = new InviteTeamMemberDTO();
        inviteTeamMemberDTO.setInvitationCode(invitationCode); // 초대 토큰 설정
        inviteTeamMemberDTO.setInvitationUrl(invitationUrl); // 초대 링크 URL 설정
        inviteTeamMemberDTO.setExpirationTime(expirationTime); // 만료 시간 설정

        return CommonApiResponse.successResponse(HttpStatus.OK, inviteTeamMemberDTO); // 성공 응답 반환
    }

    public boolean validateInvitation(String invitationCode) {
        // 초대 토큰 유효성 검증 로직 추가
        // 만료 시간 확인 및 팀 정보 반환
        // 예시로 항상 유효하다고 가정
        return true;
    }
}
