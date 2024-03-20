package aws.retrospective.controller;

import aws.retrospective.dto.SurveyDto;
import aws.retrospective.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @GetMapping
    public String showSurveyForm() {
        // 설문지 폼을 렌더링하는 뷰 반환
        return null; // 설문지 폼 루트 반환
    }

    @PostMapping("/surveys/{surveyId}/responses")
    public ResponseEntity<String> addSurvey(@RequestBody SurveyDto surveyDto) {
        surveyService.addSurvey(surveyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Survey added successfully");
    }
}
