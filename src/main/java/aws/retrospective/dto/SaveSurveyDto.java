package aws.retrospective.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class SaveSurveyDto {
    private Long surveyId;

    @NotNull
    private Integer age;

    @NotEmpty
    private String gender;

    @NotEmpty
    private String job;

    @NotEmpty
    private String residence;

    @NotEmpty
    private String discoverySource;

    private List<String> purpose;
}