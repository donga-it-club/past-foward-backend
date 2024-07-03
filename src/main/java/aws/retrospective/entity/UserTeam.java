package aws.retrospective.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private LocalDateTime joinedAt;

    @Enumerated(value = EnumType.STRING)
    UserTeamRole role;


    @Builder
    public UserTeam(User user, Team team, UserTeamRole role) {
        this.user = user;
        this.team = team;
        this.joinedAt = LocalDateTime.now();
        this.role = role;
    }

    // 리더 역할로 변경
    public void updateLeader() {
        this.role = UserTeamRole.LEADER;
    }

    // 멤버 역할로 변경
    public void updateMember() {
        this.role = UserTeamRole.MEMBER;
    }
}
