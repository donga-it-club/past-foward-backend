package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;

import aws.retrospective.dto.SaveSurveyDto;
import aws.retrospective.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/surveys")
@Tag(name = "surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping("/{surveyId}/response")
    @Operation(summary = "설문조사 저장")
    public CommonApiResponse<String> addSurvey(@Valid @RequestBody SaveSurveyDto surveyDto) {
        try {
            surveyService.addSurvey(surveyDto);
            return (CommonApiResponse.successResponse(HttpStatus.OK, "Survey added succesfully"));
        } catch (Exception e) {
            return (CommonApiResponse.errorResponse(HttpStatus.BAD_REQUEST,
                "Failed to add survey"));
        }
    }

    @Operation(summary = "설문조사 조회")
    @GetMapping
    public CommonApiResponse<List<SaveSurveyDto>> getAllSurveys() {
        List<SaveSurveyDto> surveys = (List<SaveSurveyDto>) surveyService.getAllSurveys();

        // 정상적으로 데이터를 조회한 경우 successResponse 메서드로 응답을 구성
        return CommonApiResponse.successResponse(HttpStatus.OK, surveys);
    }
}
