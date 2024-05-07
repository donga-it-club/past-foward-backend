package aws.retrospective.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemplateSection extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,  cascade = CascadeType.PERSIST)
    @JoinColumn(name = "template_id")
    @NotNull
    private RetrospectiveTemplate template;

    @NotNull
    private SectionTemplateStatus templateStatus; // 예: K, P, T

    @NotNull
    private int sequence; // 섹션 순서

    @Builder
    public TemplateSection(RetrospectiveTemplate template, SectionTemplateStatus templateStatus, Integer sequence) {
        this.template = template;
        this.templateStatus = templateStatus;
        this.sequence = sequence;
    }
}
