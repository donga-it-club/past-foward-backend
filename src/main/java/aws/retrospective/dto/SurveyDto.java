package aws.retrospective.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class SurveyDto {

    @NotNull
    private Integer age;

    @NotEmpty
    private String gender;

    @NotEmpty
    private String occupation;

    @NotEmpty
    private String region;

    @NotEmpty
    private String source;

    @NotEmpty
    private List<String> purposes;

    // 이메일 수신동의 여부
    @NotNull
    private Boolean emailConsent;

}