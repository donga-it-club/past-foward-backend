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
