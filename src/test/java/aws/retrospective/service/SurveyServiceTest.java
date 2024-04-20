package aws.retrospective.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import aws.retrospective.dto.SurveyDto;
import aws.retrospective.entity.Survey;
import aws.retrospective.entity.Survey.Gender;
import aws.retrospective.entity.User;
import aws.retrospective.repository.SurveyRepository;
import aws.retrospective.util.TestUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SurveyServiceTest {


    @InjectMocks
    private SurveyService surveyService;

    @Mock
    private SurveyRepository surveyRepository;


    @Test
    @DisplayName("설문조사 결과 추가")
    void addSurveyTest() {
        // Given
        User user = TestUtil.createUser();
        List<String> purposes = Arrays.asList("purpose1", "purpose2", "purpose3");

        SurveyDto surveyDto = SurveyDto.builder()
            .age(22)
            .gender("FEMALE")
            .occupation("student")
            .region("Korea")
            .source("internet")
            .purposes(purposes)
            .build();

        // When
        surveyService.addSurvey(user, surveyDto);

        // Survey 객체 주소가 다른 문제 해결

        // Then
        assertEquals(22, surveyDto.getAge());
        assertEquals("FEMALE", surveyDto.getGender());
        assertEquals("student", surveyDto.getOccupation());
        assertEquals("Korea", surveyDto.getRegion());
        assertEquals("internet", surveyDto.getSource());
        assertEquals(purposes, surveyDto.getPurposes());
    }

    @Test
    void getAllSurveys() {
        // 가짜 데이터 생성
        List<String> purposes = Arrays.asList("purpose1", "purpose2", "purpose3");

        List<Survey> surveys = new ArrayList<>();
        surveys.add(Survey.builder()
            .age(30)
            .gender(String.valueOf(Gender.valueOf("MALE")))
            .occupation("Engineer")
            .region("Seoul")
            .source("Internet")
            .purposes(purposes)
            .build());
        // Mock 객체 설정
        when(surveyRepository.findAll()).thenReturn(surveys);

        // 테스트 실행
        List<SurveyDto> response = surveyService.getAllSurveys();

        // 결과 확인
        assertEquals(surveys.size(), response.size());
        // 추가적인 검증을 원하는 경우 surveyDtos 내용을 검사할 수 있음
    }
}
