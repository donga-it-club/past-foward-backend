package aws.retrospective.controller;

import aws.retrospective.entity.Survey;
import aws.retrospective.repository.SurveyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyRepository surveyRepository;

    @GetMapping
    public String showSurveyForm() {
        // 설문지 폼을 렌더링하는 뷰 반환
        return null; // 설문지 폼 루트 반환
    }

    // 설문지 결과 등록
    @PostMapping("surveys/{surveysId}")
    public String addSurvey(@RequestParam Integer age, @RequestParam String gender,
        @RequestParam String job, @RequestParam String residence,
        @RequestParam String discoverySource, @RequestParam List<String> purpose) {

        Survey survey = Survey.builder()
            .age(age)
            .gender(gender)
            .job(job)
            .residence(residence)
            .discoverySource(discoverySource)
            .purpose(purpose)
            .build();

        surveyRepository.save(survey);

        return null; // 뷰 반환
    }
}
