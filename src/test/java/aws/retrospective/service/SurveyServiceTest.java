package aws.retrospective.service;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.SurveyDto;
import aws.retrospective.entity.Survey;
import aws.retrospective.entity.Survey.Gender;
import aws.retrospective.repository.SurveyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SurveyServiceTest {

    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private SurveyService surveyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
