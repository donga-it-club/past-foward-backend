package aws.retrospective.service;

import aws.retrospective.dto.AcceptInvitationDto;
import aws.retrospective.dto.AcceptInviteResponseDto;
import aws.retrospective.dto.InviteTeamMemberDTO;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.TeamInvite;
import aws.retrospective.entity.User;
import aws.retrospective.entity.UserTeam;
import aws.retrospective.entity.UserTeamRole;
import aws.retrospective.repository.TeamInvitationRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.UserTeamRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InviteTeamMemberService {

    private final TeamRepository teamRepository;
    private final TeamInvitationRepository teamInvitationRepository;
    private final QRCodeService qrCodeService;
    private final UserTeamRepository userTeamRepository;
    private final UserTeamSaveHelper userTeamSaveHelper;
    private static final int EXPIRATION_HOURS = 2;

    @Value("${domain.url}")
    private String domainUrl;

    @Transactional
    public AcceptInviteResponseDto acceptInvitation(AcceptInvitationDto dto, User user) {
        String invitationCode = dto.getInvitationCode();
        validateInvitation(invitationCode);
        TeamInvite teamInvite = teamInvitationRepository.findByInvitationCode(invitationCode)
            .orElseThrow(
                () -> new IllegalArgumentException("Invalid invitation code: " + invitationCode));

        Team team = teamInvite.getTeam();

        // fast-path: 이미 가입된 경우 조기 반환 (동시성 가드는 DB 유니크 제약조건이 담당)
        Optional<UserTeam> existingUserTeam = userTeamRepository.findByTeamIdAndUserId(team.getId(),
            user.getId());
        if (existingUserTeam.isPresent()) {
            return AcceptInviteResponseDto.builder()
                .teamId(team.getId())
                .userId(user.getId())
                .role(existingUserTeam.get().getRole())
                .build();
        }

        UserTeam newUserTeam = UserTeam.builder()
            .user(user)
            .team(team)
            .role(UserTeamRole.MEMBER)
            .build();

        // REQUIRES_NEW 트랜잭션으로 save 시도 → 중복 키 위반 시 내부 트랜잭션만 롤백
        boolean saved = userTeamSaveHelper.saveIfNotDuplicate(newUserTeam);

        if (!saved) {
            // 동시 요청 경쟁에서 밀린 경우: 먼저 저장된 레코드 조회 후 반환
            UserTeam created = userTeamRepository.findByTeamIdAndUserId(team.getId(), user.getId())
                .orElseThrow(() -> new IllegalStateException(
                    "UserTeam not found after duplicate key violation for teamId="
                        + team.getId() + ", userId=" + user.getId()));
            return AcceptInviteResponseDto.builder()
                .teamId(team.getId())
                .userId(user.getId())
                .role(created.getRole())
                .build();
        }

        return AcceptInviteResponseDto.builder()
            .teamId(team.getId())
            .userId(user.getId())
            .role(UserTeamRole.MEMBER)
            .build();

    }


    public InviteTeamMemberDTO generateInvitation(Long teamId) {
        // 팀 조회
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new IllegalArgumentException("Team not found for id: " + teamId));

        // 초대 코드 생성
        String invitationCode = UUID.randomUUID().toString();
        // 초대 만료 시간 설정
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(EXPIRATION_HOURS);

        // 초대 정보를 데이터베이스에 저장
        TeamInvite teamInvite = TeamInvite.builder()
            .team(team)
            .invitationCode(invitationCode)
            .expirationTime(expirationTime)
            .build();
        teamInvitationRepository.save(teamInvite);

        // 초대 링크 생성
        String invitationUrl = domainUrl + "/invitations/" + invitationCode;

        // 초대 링크를 QR 코드로 변환
        byte[] qrCodeImage = qrCodeService.generateQRCode(invitationUrl, expirationTime);

        // 초대 정보 DTO 생성
        if (expirationTime.isBefore(LocalDateTime.now())) {
            invitationCode = UUID.randomUUID().toString();
            invitationUrl = domainUrl + "/invitations/" + invitationCode;
            expirationTime = LocalDateTime.now().plusHours(EXPIRATION_HOURS);
            qrCodeImage = qrCodeService.generateQRCode(invitationUrl, expirationTime);
        }
        return new InviteTeamMemberDTO(invitationCode, invitationUrl, expirationTime, qrCodeImage);
    }

    // 초대 토큰 유효성을 검증하는 메서드
    void validateInvitation(String invitationCode) {
        TeamInvite teamInvite = (TeamInvite) teamInvitationRepository.findByInvitationCode(
                invitationCode)
            .orElseThrow(
                () -> new IllegalArgumentException("Invalid invitation code: " + invitationCode));

        if (teamInvite.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invitation code has expired: " + invitationCode);
        }
    }
}
