package aws.retrospective.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Survey extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surveyId; // 설문 아이디 - PK

    // 설문조사 관련 필드 추가
    private String age;

    @Enumerated(EnumType.STRING)
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

