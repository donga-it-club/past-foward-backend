package aws.retrospective.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Retrospective extends BaseEntity {

    @Id
    @Column(name = "retrospective_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 회고 제목

    @Enumerated(value = EnumType.STRING)
    private RetrospectiveStatus retrospectiveStatus; // 회고 유형(KPT, KWAT)

    @Enumerated(value = EnumType.STRING)
    private ProjectStatus status; // 회고 진행 상태(시작 전, 진행 중, 완료)

    @Builder
    public Retrospective(String title, RetrospectiveStatus retrospectiveStatus,
        ProjectStatus status) {
        this.title = title;
        this.retrospectiveStatus = retrospectiveStatus;
        this.status = status;
    }
}