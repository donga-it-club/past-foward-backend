package aws.retrospective.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long id; // 설문 아이디 - PK


    // 설문조사 관련 필드 추가
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String occupation;

    private String region;

    private String source;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> purpose; // 복수 선택 가능한 항목을 리스트로 저장

    private String otherPurpose; // '기타' 부분 입력값

    public enum Gender {
        MALE,
        FEMALE
    }

    @Builder
    public Survey(Integer age, String gender, String occupation, String region,
        String source, List<String> purpose, String otherPurpose) {
        this.age = age;
        this.gender = Gender.valueOf(gender);
        this.occupation = occupation;
        this.region = region;
        this.source = source;
        this.purpose = purpose;
        this.otherPurpose = otherPurpose;
    }
}
