package aws.retrospective.service;

import aws.retrospective.dto.SurveyDto;
import aws.retrospective.entity.Survey;
import aws.retrospective.entity.User;
import aws.retrospective.repository.SurveyRepository;
import java.util.List;
import java.util.stream.Collectors;

import aws.retrospective.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;

    // 설문지 결과 등록
    @Transactional
    public void addSurvey(User user, SurveyDto dto) {
        checkIfAlreadySubmitted(user);

        Survey survey = Survey.builder()
            .user(user)
            .age(dto.getAge())
            .gender(dto.getGender())
            .occupation(dto.getOccupation())
            .region(dto.getRegion())
            .source(dto.getSource())
            .purposes(dto.getPurposes())
            .build();

        surveyRepository.save(survey);

        // 이메일 수신 동의 여부 업데이트
        user.updateEmailConsent(dto.getEmailConsents());
        userRepository.save(user);
    }

    public List<SurveyDto> getAllSurveys() {
        List<Survey> surveys = surveyRepository.findAll();
        return surveys.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    // 성별, 연령대 (기본 정보) 데이터를 가져오는 메서드
    public List<SurveyDto> getGenderAndAgeSurveys() {
        List<Survey> surveys = surveyRepository.findAll();

        return surveys.stream()
                .map(survey -> SurveyDto.builder()
                        .age(survey.getAge())
                        .gender(String.valueOf(survey.getGender()))
                        .build())
                .collect(Collectors.toList());
    }

    // 직업, 지역 데이터를 가져오는 메서드
    public List<SurveyDto> getOccupationAndRegionSurveys() {
        List<Survey> surveys = surveyRepository.findAll();

        return surveys.stream()
                .map(survey -> SurveyDto.builder()
                        .occupation(survey.getOccupation())
                        .region(survey.getRegion())
                        .build())
                .collect(Collectors.toList());
    }

    // 서비스를 알게된 경로 및 서비스 사용목적, 이메일 수신동의 여부를 가져오는 메서드
    public List<SurveyDto> getSourceAndPurposeSurveys() {
        List<Survey> surveys = surveyRepository.findAll();

        return surveys.stream()
                .map(survey -> SurveyDto.builder()
                        .source(survey.getSource())
                        .purposes(survey.getPurposes())
                        .emailConsents(survey.getUser().isEmailConsent())
                        .build())
                .collect(Collectors.toList());
    }

    private void checkIfAlreadySubmitted(User user) {
        if (surveyRepository.existsByUser(user)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 설문조사를 제출하셨습니다.");
        }

    }

    private SurveyDto convertToDto(Survey survey) {
        return SurveyDto.builder()
            .age(survey.getAge())
            .gender(survey.getGender().toString())
            .occupation(survey.getOccupation())
            .region(survey.getRegion())
            .source(survey.getSource())
            .purposes(survey.getPurposes())
            .emailConsents(survey.getUser().isEmailConsent())
            .build();
    }
}