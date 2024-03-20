package aws.retrospective.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class SurveyDto {
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
