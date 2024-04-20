package aws.retrospective.service;

import aws.retrospective.dto.SurveyDto;
import aws.retrospective.entity.Survey;
import aws.retrospective.repository.SurveyRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    // 설문지 결과 등록
    @Transactional
    public void addSurvey(@Valid SurveyDto request) {

        Survey survey = Survey.builder()
            .age(request.getAge())
            .gender(request.getGender())
            .occupation(request.getOccupation())
            .region(request.getRegion())
            .source(request.getSource())
            .purposes(request.getPurposes())
            .build();

        surveyRepository.save(survey);
    }

    public List<SurveyDto> getAllSurveys() {
        List<Survey> surveys = surveyRepository.findAll();
        return surveys.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    private SurveyDto convertToDto(Survey survey) {
        return SurveyDto.builder()
            .age(survey.getAge())
            .gender(survey.getGender().toString())
            .occupation(survey.getOccupation())
            .region(survey.getRegion())
            .source(survey.getSource())
            .purposes(survey.getPurposes())
            .build();
    }
}