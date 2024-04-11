package aws.retrospective.service;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.SaveSurveyDto;
import aws.retrospective.entity.Survey;
import aws.retrospective.repository.SurveyRepository;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;

import aws.retrospective.entity.Survey.Gender;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;


@SpringBootTest
@Transactional
public class SurveyServiceTest {

    @InjectMocks
    private SurveyService surveyService;

    @Mock
    private SurveyRepository surveyRepository;
  
  
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
  

    @Test
    @DisplayName("설문조사 결과 추가")
    void addSurveyTest() {
        // Given
        SaveSurveyDto surveyDto = SaveSurveyDto.builder()
            .age(22)
            .gender("female")
            .job("student")
            .residence("Korea")
            .discoverySource("internet")
            .purpose(List.of("research", "analysis"))
            .build();

        // When
        surveyService.addSurvey(surveyDto);

        // Survey 객체 주소가 다른 문제 해결

        // Then
        verify(surveyRepository).save(any(Survey.class));

        assertEquals(22, surveyDto.getAge());
        assertEquals("female", surveyDto.getGender());
        assertEquals("student", surveyDto.getJob());
        assertEquals("Korea", surveyDto.getResidence());
        assertEquals("internet", surveyDto.getDiscoverySource());
        assertTrue(surveyDto.getPurpose().containsAll(List.of("research", "analysis")));
    }

    @Test
    void getAllSurveys() {
        // 가짜 데이터 생성
        List<Survey> surveys = new ArrayList<>();
        surveys.add(Survey.builder()
            .age("30")
            .gender(Gender.valueOf("MALE"))
            .occupation("Engineer")
            .region("Seoul")
            .source("Internet")
            .purpose("Research")
            .build());
        // Mock 객체 설정
        when(surveyRepository.findAll()).thenReturn(surveys);

        // 테스트 실행
        CommonApiResponse<List<SurveyDto>> response = surveyService.getAllSurveys();

        // 결과 확인
        assertEquals(surveys.size(), response.getData().size());
        // 추가적인 검증을 원하는 경우 surveyDtos 내용을 검사할 수 있음
    }
}
