package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.SurveyDto;
import aws.retrospective.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/surveys")
@Tag(name = "surveys")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Operation(summary = "설문조사 조회")
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<SurveyDto>>> getAllSurveys() {
        List<SurveyDto> surveys = surveyService.getAllSurveys();

        // 정상적으로 데이터를 조회한 경우 successResponse 메서드로 응답을 구성
        return ResponseEntity.ok(CommonApiResponse.successResponse(HttpStatus.OK, surveys));
    }
}
