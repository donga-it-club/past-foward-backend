package aws.retrospective.service;

import aws.retrospective.dto.SurveyDto;
import aws.retrospective.entity.Survey;
import aws.retrospective.repository.SurveyRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    public List<SurveyDto> getAllSurveys() {
        List<Survey> surveys = surveyRepository.findAll();
        return surveys.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    private SurveyDto convertToDto(Survey survey) {
        return SurveyDto.builder()
            .id(survey.getId())
            .age(survey.getAge())
            .gender(survey.getGender().toString())
            .occupation(survey.getOccupation())
            .region(survey.getRegion())
            .source(survey.getSource())
            .purpose(survey.getPurpose())
            .build();
    }
}
