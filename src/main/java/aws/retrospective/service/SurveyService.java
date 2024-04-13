package aws.retrospective.service;

import aws.retrospective.dto.SaveSurveyDto;
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

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    // 설문지 결과 등록
    @Transactional
    public void addSurvey(@Valid SaveSurveyDto request) {

        Survey survey = Survey.builder()
            .age(request.getAge())
            .gender(request.getGender())
            .job(request.getJob())
            .residence(request.getResidence())
            .discoverySource(request.getDiscoverySource())
            .purpose(request.getPurpose())
            .build();

        surveyRepository.save(survey);
    }

    public CommonApiResponse<List<SaveSurveyDto>> getAllSurveys() {
        List<Survey> surveys = surveyRepository.findAll();
        List<SaveSurveyDto> surveyDtos = surveys.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        return CommonApiResponse.successResponse(HttpStatus.OK, surveyDtos);
    }

    private SaveSurveyDto convertToDto(Survey survey) {
        return SaveSurveyDto.builder()
            .age(Integer.valueOf(String.valueOf(survey.getAge())))
            .gender(survey.getGender().toString())
            .job(survey.getJob())
            .residence(survey.getResidence())
            .discoverySource(survey.getDiscoverySource())
            .purpose(Collections.singletonList(survey.getPurpose().toString()))
            .build();
    }
}
