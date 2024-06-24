package aws.retrospective.entity;

import
    jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE retrospective SET deleted_date = CURRENT_TIMESTAMP WHERE retrospective_id = ?")
@SQLRestriction("deleted_date IS NULL")
public class Retrospective extends BaseEntity {

    @Id
    @Column(name = "retrospective_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title; // 회고 제목

    private UUID thumbnail; // 회고 썸네일

    private String description; // 회고 설명

    private LocalDateTime startDate; // 회고 시작 일자

    private LocalDateTime deletedDate; // 삭제 일자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team; // 회고를 작성한 팀 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user; // 회고를 작성한 사용자 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "template_id")
    private RetrospectiveTemplate template; // 회고 템플릿 정보

    @Enumerated(value = EnumType.STRING)
    private ProjectStatus status; // 회고 진행 상태(시작 전, 진행 중, 완료)

    @OneToMany(mappedBy = "retrospective")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "retrospectiveGroup_id")
    private RetrospectiveGroup retrospectiveGroup;

    @Builder
    public Retrospective(String title, UUID thumbnail,
        String description,
        LocalDateTime deletedDate,
        ProjectStatus status, Team team,
        User user,
        RetrospectiveTemplate template,
        LocalDateTime startDate
    ) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.description = description;
        this.deletedDate = deletedDate;
        this.status = status;
        this.team = team;
        this.user = user;
        this.template = template;
        this.startDate = startDate;
    }

    public boolean isOwnedByUser(Long userId) {
        return this.user.getId().equals(userId);
    }

    public void update(String title, ProjectStatus status, UUID thumbnail, String description) {
        this.title = title;
        this.status = status;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    public boolean isSameTemplate(TemplateSection templateSection) {
        return this.getTemplate().getName().equals(templateSection.getTemplate().getName());
    }

    public boolean isSameTeam(Team team) {
        return this.team.getId().equals(team.getId());
    }
}