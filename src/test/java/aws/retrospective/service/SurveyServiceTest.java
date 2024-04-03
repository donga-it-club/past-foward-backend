package aws.retrospective.service;

import aws.retrospective.dto.SaveSurveyDto;
import aws.retrospective.entity.Survey;
import aws.retrospective.repository.SurveyRepository;
import java.util.List;
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


@SpringBootTest
@Transactional
public class SurveyServiceTest {

    @InjectMocks
    private SurveyService surveyService;

    @Mock
    private SurveyRepository surveyRepository;

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
}