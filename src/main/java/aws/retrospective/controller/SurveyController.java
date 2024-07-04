package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.common.CurrentUser;
import aws.retrospective.dto.SurveyDto;
import aws.retrospective.entity.User;
import aws.retrospective.service.SurveyService;
import aws.retrospective.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/surveys")
@Tag(name = "surveys")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class SurveyController {

    private final SurveyService surveyService;
    private final UserService userService;

    @PostMapping("/responses")
    @Operation(summary = "설문조사 저장", description = "설문조사 결과를 DB에 저장하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "설문조사가 성공적으로 저장됨")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addSurvey(@CurrentUser User user, @Valid @RequestBody SurveyDto surveyDto) {
        surveyService.addSurvey(user, surveyDto);
    }

    /*
    @Operation(summary = "설문조사 조회", description = "설문조사 데이터를 조회하는 API")
    @ApiResponse(responseCode = "200", description = "설문조사가 성공적으로 조회됨")
    @GetMapping()
    public CommonApiResponse<List<SurveyDto>> getAllSurveys() {
        List<SurveyDto> surveys = surveyService.getAllSurveys();

        // 정상적으로 데이터를 조회한 경우 successResponse 메서드로 응답을 구성
        return CommonApiResponse.successResponse(HttpStatus.OK, surveys);
    }
     */

    @Operation(summary = "성별 및 연령 조회", description = "설문조사에서 성별 및 연령 데이터를 조회하는 API")
    @ApiResponse(responseCode = "200", description = "성별 및 연령 데이터가 성공적으로 조회됨")
    @GetMapping("/basicInfo")
    public CommonApiResponse<List<SurveyDto>> getGenderAndAgeSurveys() {
        List<SurveyDto> surveys = surveyService.getGenderAndAgeSurveys();

        // 정상적으로 데이터를 조회한 경우 successResponse 메서드로 응답 구성
        return CommonApiResponse.successResponse(HttpStatus.OK, surveys);
    }

    @Operation(summary = "직업 및 지역 조회", description = "설문조사에서 직업 및 지역 데이터를 조회하는 API")
    @ApiResponse(responseCode = "200", description = "직업 및 지역 데이터가 성공적으로 조회됨")
    @GetMapping("/workingInfo")
    public CommonApiResponse<List<SurveyDto>> getOccupationAndRegionSurveys() {
        List<SurveyDto> surveys = surveyService.getOccupationAndRegionSurveys();

        // 정상적으로 데이터를 조회한 경우 successResponse 메서드로 응답 구성
        return CommonApiResponse.successResponse(HttpStatus.OK, surveys);
    }

    @Operation(summary = "서비스를 알게된 경로, 서비스 사용 목적, 이메일 수신 여부 조회", description = "설문조사에서 서비스 관련 데이터를 조회하는 API")
    @ApiResponse(responseCode = "200", description = "서비스 관련 데이터가 성공적으로 조회됨")
    @GetMapping("/serviceInfo")
    public CommonApiResponse<List<SurveyDto>> getGetSourceAndPurposeSurveys() {
        List<SurveyDto> surveys = surveyService.getSourceAndPurposeSurveys();

        // 정상적으로 데이터를 조회한 경우 successResponse 메서드로 응답 구성
        return CommonApiResponse.successResponse(HttpStatus.OK, surveys);
    }
}
