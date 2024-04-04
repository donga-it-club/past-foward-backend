package aws.retrospective.service;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.InviteTeamMemberDTO;
import aws.retrospective.entity.Team;
import aws.retrospective.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteTeamMemberService {

    private final TeamRepository teamRepository;
    private final QRCodeService qrCodeService;
    private static final int EXPIRATION_HOURS = 2;

    @Value("${domain.url}")
    private String domainUrl;


    public CommonApiResponse<InviteTeamMemberDTO> generateInvitation(Long teamId) {
        // 팀 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found for id: " + teamId));

        // 초대 코드 생성
        String invitationCode = UUID.randomUUID().toString();
        // 초대 링크 생성
        String invitationUrl = domainUrl + "/invitations/" + invitationCode;
        // 초대 만료 시간 설정
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(EXPIRATION_HOURS);

        // 초대 토큰 유효성 검증
        validateInvitation(invitationCode);

        // 초대 링크를 QR 코드로 변환
        byte[] qrCodeImage = qrCodeService.generateQRCode(invitationUrl, expirationTime);

        // 초대 정보 DTO 생성
        if (expirationTime.isBefore(LocalDateTime.now())) {
            invitationCode = UUID.randomUUID().toString();
            invitationUrl = domainUrl + "/invitations/" + invitationCode;
            expirationTime = LocalDateTime.now().plusHours(EXPIRATION_HOURS);
            qrCodeImage = qrCodeService.generateQRCode(invitationUrl, expirationTime);
        }
        InviteTeamMemberDTO inviteTeamMemberDTO = new InviteTeamMemberDTO(invitationCode, invitationUrl, expirationTime, qrCodeImage);

        // HTTP 상태코드 OK로 응답
        return CommonApiResponse.successResponse(HttpStatus.OK, inviteTeamMemberDTO);
    }

    // 초대 토큰 유효성을 검증하는 메서드
    private void validateInvitation(String invitationCode) {
        // 초대 토큰 유효성 검증 로직 추가
        // 예시로 항상 유효하다고 가정
        boolean isValid = true; // 유효성 검증 결과
        if (!isValid) {
            // 유효하지 않은 경우 IllegalArgumentException을 던짐
            throw new IllegalArgumentException("Invalid invitation code: " + invitationCode);
        }
    }
}
