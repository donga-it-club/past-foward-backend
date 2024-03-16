package aws.retrospective.controller;


import aws.retrospective.common.ApiResponse;
import aws.retrospective.dto.CreateRetrospectiveDto;
import aws.retrospective.dto.CreateRetrospectiveResponseDto;
import aws.retrospective.dto.GetRetrospectivesDto;
import aws.retrospective.dto.PaginationResponseDto;
import aws.retrospective.dto.RetrospectiveResponseDto;
import aws.retrospective.service.RetrospectiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/retrospectives")
@Tag(name = "retrospectives")
public class RetrospectiveController {

    private final RetrospectiveService retrospectiveService;


    @Operation(summary = "회고 조회")
    @GetMapping()
    public ApiResponse<PaginationResponseDto<RetrospectiveResponseDto>> getRetrospectives(
        @Valid GetRetrospectivesDto dto) {
        PaginationResponseDto<RetrospectiveResponseDto> response = retrospectiveService.getRetrospectives(
            dto);

        return ApiResponse.successResponse(HttpStatus.OK, response);
    }

    @Operation(summary = "회고 생성")
    @PostMapping()
    public ApiResponse<CreateRetrospectiveResponseDto> createRetrospective(
        @RequestBody @Valid CreateRetrospectiveDto createRetrospectiveDto) {
        CreateRetrospectiveResponseDto response = retrospectiveService.createRetrospective(
            createRetrospectiveDto);

        return ApiResponse.successResponse(HttpStatus.CREATED, response);

    }
}
