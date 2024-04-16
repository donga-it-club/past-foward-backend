package aws.retrospective.entity;

import jakarta.persistence.*;
import java.util.List;
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
    @Column(name = "survey_id")
    private Long id; // 설문 아이디 - PK

    private Integer age; // 연령

    @Enumerated(EnumType.STRING)
    private String gender; // 성별

    private String occupation; // 직업

    private String region; // 거주 지역

    private String source; // 서비스 발견 경로

    private String purpose; // 서비스 사용 목적

    public enum Gender {
        MALE,
        FEMALE
    }

    @Builder
    public Survey(Integer age, String gender, String occupation, String region,
        String source, String purpose) {
        this.age = age;
        this.gender = gender;
        this.occupation = occupation;
        this.region = region;
        this.source = source;
        this.purpose = purpose;
    }
}

