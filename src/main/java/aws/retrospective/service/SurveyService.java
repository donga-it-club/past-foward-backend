package aws.retrospective.service;

import aws.retrospective.dto.SurveyDto;
import aws.retrospective.entity.Survey;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.repository.SurveyRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static sun.awt.image.MultiResolutionCachedImage.map;

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
            .purpose(request.getPurpose())
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
            .age(Integer.valueOf(String.valueOf(survey.getAge())))
            .gender(survey.getGender().toString())
            .occupation(survey.getOccupation())
            .region(survey.getRegion())
            .source(survey.getSource())
            .purpose(survey.getPurpose())
            .build();
    }
}
