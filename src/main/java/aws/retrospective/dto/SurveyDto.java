package aws.retrospective.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Builder
public class SurveyDto {
    private Long surveyId;

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

    private String purpose;

    public void setSurveyId(String surveyId) {
    }
}