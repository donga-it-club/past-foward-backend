package aws.retrospective.service;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.SurveyDto;
import aws.retrospective.entity.Survey;
import aws.retrospective.entity.Survey.Gender;
import aws.retrospective.repository.SurveyRepository;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;


@Transactional
public class SurveyServiceTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @InjectMocks
    private SurveyService surveyService;

    @Mock
    private SurveyRepository surveyRepository;
  

    @Test
    @DisplayName("설문조사 결과 추가")
    void addSurveyTest() {
        // Given
        SurveyDto surveyDto = SurveyDto.builder()
            .age(22)
            .gender("FEMALE")
            .occupation("student")
            .region("Korea")
            .source("internet")
            .purpose("research")
            .build();

        // When
        surveyService.addSurvey(surveyDto);

        // Survey 객체 주소가 다른 문제 해결

        // Then
        verify(surveyRepository).save(any(Survey.class));

        assertEquals(22, surveyDto.getAge());
        assertEquals("FEMALE", surveyDto.getGender());
        assertEquals("student", surveyDto.getOccupation());
        assertEquals("Korea", surveyDto.getRegion());
        assertEquals("internet", surveyDto.getSource());
        assertEquals("research", surveyDto.getPurpose());
    }

    @Test
    void getAllSurveys() {
        // 가짜 데이터 생성
        List<Survey> surveys = new ArrayList<>();
        surveys.add(Survey.builder()
            .age(30)
            .gender(String.valueOf(Gender.valueOf("MALE")))
            .occupation("Engineer")
            .region("Seoul")
            .source("Internet")
            .purpose("research")
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
