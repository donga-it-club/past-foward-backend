package aws.retrospective.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class SaveSurveyDto {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surveyId;

    @NotEmpty
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