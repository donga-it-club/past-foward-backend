package aws.retrospective.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SurveyDto {

    private final Long id;
    private final String age;
    private final String gender;
    private final String occupation;
    private final String region;
    private final String source;
    private final String purpose;
}
