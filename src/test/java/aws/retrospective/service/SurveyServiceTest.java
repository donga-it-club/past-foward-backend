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
import static org.assertj.core.api.Assertions.assertThat;

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

        // Then
        verify(surveyRepository).save(new Survey(surveyDto.getAge(), surveyDto.getGender(),
            surveyDto.getJob(), surveyDto.getResidence(), surveyDto.getDiscoverySource(),
            surveyDto.getPurpose()));

        // Additional assertions
        assertThat(surveyDto.getAge()).isEqualTo(22);
        assertThat(surveyDto.getGender()).isEqualTo("female");
        assertThat(surveyDto.getJob()).isEqualTo("student");
        assertThat(surveyDto.getResidence()).isEqualTo("Korea");
        assertThat(surveyDto.getDiscoverySource()).isEqualTo("internet");
        assertThat(surveyDto.getPurpose()).containsExactly("research", "analysis");
    }
}