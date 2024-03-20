package aws.retrospective.service;

import aws.retrospective.dto.SurveyDto;
import aws.retrospective.entity.Survey;
import aws.retrospective.repository.SurveyRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("설문조사 결과 추가")
    void addSurvey() {
        // Given
        SurveyDto surveyDto = new SurveyDto();
        surveyDto.setAge(22);
        surveyDto.setGender("female");
        surveyDto.setJob("student");
        surveyDto.setResidence("Korea");
        surveyDto.setDiscoverySource("internet");
        surveyDto.setPurpose(List.of("research", "analysis"));

        // When
        surveyService.addSurvey(surveyDto);

        // Then
        verify(surveyRepository).save(new Survey(surveyDto.getAge(), surveyDto.getGender(),
            surveyDto.getJob(), surveyDto.getResidence(), surveyDto.getDiscoverySource(),
            surveyDto.getPurpose()));

        // Additional assertions
        assertThat(surveyDto.getAge()).isEqualTo(30);
        assertThat(surveyDto.getGender()).isEqualTo("male");
        assertThat(surveyDto.getJob()).isEqualTo("engineer");
        assertThat(surveyDto.getResidence()).isEqualTo("New York");
        assertThat(surveyDto.getDiscoverySource()).isEqualTo("internet");
        assertThat(surveyDto.getPurpose()).containsExactly("research", "analysis");
    }
}
