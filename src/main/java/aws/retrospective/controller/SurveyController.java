package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.SaveSurveyDto;
import aws.retrospective.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @Valid
    @PostMapping("/{surveyId}/response")
    @Operation(summary = "설문조사 저장")
    public CommonApiResponse<String> addSurvey(@RequestBody SaveSurveyDto surveyDto) {
        surveyService.addSurvey(surveyDto);
        try {
            surveyService.addSurvey(surveyDto);
            return (CommonApiResponse.successResponse(HttpStatus.OK, "Survey added succesfully"));
        } catch (Exception e) {
            return (CommonApiResponse.errorResponse(HttpStatus.BAD_REQUEST,
                "Failed to add survey"));
        }
    }
}