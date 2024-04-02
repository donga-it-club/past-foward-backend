package aws.retrospective.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surveyId; // 설문 아이디 - PK

    @NotNull
    private Integer age; // 연령

    @NotEmpty
    private String gender; // 성별

    @NotEmpty
    private String job; // 직업

    @NotEmpty
    private String residence; // 거주 지역

    @NotEmpty
    private String discoverySource; // 서비스 발견 경로

    @ElementCollection
    private List<String> purpose; // 서비스 사용 목적

    @Builder
    public Survey(Integer age, String gender, String job, String residence,
        String discoverySource, List<String> purpose) {
        this.age = age;
        this.gender = gender;
        this.job = job;
        this.residence = residence;
        this.discoverySource = discoverySource;
        this.purpose = purpose;
    }
}