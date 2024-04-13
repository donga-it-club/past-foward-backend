package aws.retrospective.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="TeamInvite")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamInvite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키를 자동으로 생성
    @Column(name = "invite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Team 엔터티와의 다대일(N:1) 관계 설정
    @JoinColumn(name = "team_id") // 외래 키 설정
    private Team team;

    @Column(name = "invitation_code")
    private String invitationCode;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    @Builder
    public TeamInvite(Team team, String invitationCode, LocalDateTime expirationTime) {
        this.team = team;
        this.invitationCode = invitationCode;
        this.expirationTime = expirationTime;
    }


}
