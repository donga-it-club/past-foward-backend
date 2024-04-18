package aws.retrospective.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotEmpty.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String content; // 작성 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section; // 어떤 Section의 게시물에서 작성된 댓글인지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_comment_id")
    private SectionComment sectionComment; // 섹션 댓글 정보

    @Builder
    public Comment(String content, User user, Section section, SectionComment sectionComment) {
        this.content = content;
        this.user = user;
        this.section = section;
        this.sectionComment = sectionComment;
    }

    public void updateComment(String updateContent) {
        this.content = updateContent;
    }

}