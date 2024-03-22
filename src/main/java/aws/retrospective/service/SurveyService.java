package aws.retrospective.service;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.SurveyDto;
import aws.retrospective.entity.Survey;
import aws.retrospective.repository.SurveyRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    public CommonApiResponse<List<SurveyDto>> getAllSurveys() {
        List<Survey> surveys = surveyRepository.findAll();
        List<SurveyDto> surveyDtos = surveys.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        return CommonApiResponse.successResponse(HttpStatus.OK, surveyDtos);
    }

    private SurveyDto convertToDto(Survey survey) {
        return SurveyDto.builder()
            .id(survey.getId())
            .age(survey.getAge())
            .gender(survey.getGender())
            .occupation(survey.getOccupation())
            .region(survey.getRegion())
            .source(survey.getSource())
            .purpose(survey.getPurpose())
            .build();
    }
}
