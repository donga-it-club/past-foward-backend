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
public class Survey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long id;

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
