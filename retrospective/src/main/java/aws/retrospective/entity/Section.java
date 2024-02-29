package aws.retrospective.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section extends BaseEntity {

    @Id
    @Column(name = "section_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content; // 내용
    private long likeCnt; // 좋아요 개수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retrospective_id")
    private Retrospective retrospective; // 어떤 회고로부터 만들어진 section인지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 작성자

    @Builder
    public Section(String content, long likeCnt, Retrospective retrospective, User user) {
        this.content = content;
        this.likeCnt = likeCnt;
        this.retrospective = retrospective;
        this.user = user;
    }
}