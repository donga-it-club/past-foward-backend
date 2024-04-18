package aws.retrospective.dto;

import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.NotNull;
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

    private String purpose;
}