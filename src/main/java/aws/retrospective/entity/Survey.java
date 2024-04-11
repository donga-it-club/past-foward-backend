package aws.retrospective.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Column;
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

public class Survey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long id;

    // 설문조사 관련 필드 추가
    private String age;
    private Gender gender;
    private String occupation;
    private String region;
    private String source;
    private String purpose;

    public enum Gender {
        MALE,
        FEMALE
    }

    @Builder
    public Survey(String age, Gender gender, String occupation, String region, String source,
        String purpose) {
        this.age = age;
        this.gender = gender;
        this.occupation = occupation;
        this.region = region;
        this.source = source;
        this.purpose = purpose;
    }
}