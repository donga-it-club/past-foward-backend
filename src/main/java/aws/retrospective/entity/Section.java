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
import java.util.ArrayList;
import java.util.List;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retrospective_id")
    private Retrospective retrospective; // 어떤 회고로부터 만들어진 section인지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_section_id")
    private TemplateSection templateSection; // 섹션 템플릿 정보

    @OneToMany(mappedBy = "section", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Section(String content, Retrospective retrospective, User user,
        TemplateSection templateSection) {
        this.content = content;
        this.retrospective = retrospective;
        this.user = user;
        this.templateSection = templateSection;
    }

    // 섹션 내용 update
    public void updateSectionContent(String content) {
        this.content = content;
    }

    public boolean isSameUser(User user) {
        return this.getUser().getId().equals(user.getId());
    }

    public boolean isNotSameUser(User user) {
        return !isSameUser(user);
    }

    public boolean isActionItemsSection() {
        return this.getTemplateSection().getSectionName().equals("Action Items");
    }

    public boolean isNotActionItemsSection() {
        return !isActionItemsSection();
    }

    public boolean isNotKudosTemplate() {
        return !this.templateSection.getSectionName().equals("Kudos");
    }

    public boolean isKudosTemplate() {
        return this.templateSection.getSectionName().equals("Kudos");
    }
}