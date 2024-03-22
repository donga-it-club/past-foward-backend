package aws.retrospective.controller;


import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.CreateRetrospectiveDto;
import aws.retrospective.dto.CreateRetrospectiveResponseDto;
import aws.retrospective.dto.GetRetrospectivesDto;
import aws.retrospective.dto.PaginationResponseDto;
import aws.retrospective.dto.RetrospectiveResponseDto;
import aws.retrospective.dto.UpdateRetrospectiveDto;
import aws.retrospective.service.RetrospectiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/retrospectives")
@Tag(name = "retrospectives")
public class RetrospectiveController {

    private final RetrospectiveService retrospectiveService;


    @Operation(summary = "회고 조회")
    @GetMapping()
    public CommonApiResponse<PaginationResponseDto<RetrospectiveResponseDto>> getRetrospectives(
        @Valid GetRetrospectivesDto dto) {
        PaginationResponseDto<RetrospectiveResponseDto> response = retrospectiveService.getRetrospectives(
            dto);

        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @Operation(summary = "회고 수정")
    @PutMapping("/{retrospectiveId}")
    public CommonApiResponse<RetrospectiveResponseDto> updateRetrospective(
        @PathVariable Long retrospectiveId,
        @RequestBody @Valid UpdateRetrospectiveDto dto) {
        RetrospectiveResponseDto response = retrospectiveService.updateRetrospective(
            retrospectiveId, dto);

        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @Operation(summary = "회고 생성")
    @PostMapping()
    public CommonApiResponse<CreateRetrospectiveResponseDto> createRetrospective(
        @RequestBody @Valid CreateRetrospectiveDto createRetrospectiveDto) {
        CreateRetrospectiveResponseDto response = retrospectiveService.createRetrospective(
            createRetrospectiveDto);

        return CommonApiResponse.successResponse(HttpStatus.CREATED, response);

    }

    @Operation(summary = "회고 삭제")
    @DeleteMapping("/{retrospectiveId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRetrospective(@PathVariable Long retrospectiveId,
        @RequestParam Long userId) {
        retrospectiveService.deleteRetrospective(retrospectiveId, userId);
    }
}
