package aws.retrospective.service;

import aws.retrospective.dto.SaveSurveyDto;
import aws.retrospective.entity.Survey;
import aws.retrospective.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    // 설문지 결과 등록
    @Transactional
    public void addSurvey(@RequestBody SaveSurveyDto request) {

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
}