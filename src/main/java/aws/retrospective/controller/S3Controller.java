package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.GetPreSignedUrlRequestDto;
import aws.retrospective.dto.GetPreSignedUrlResponseDto;
import aws.retrospective.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
@Tag(name = "AWS S3")
@SecurityRequirement(name = "JWT")
public class S3Controller {

    private final S3Service s3Service;

    // PreSignedUrl 발급
    @PostMapping("/pre-signed-url")
    @Operation(summary = "AWS S3 PreSignedUrl 발급")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "PreSignedUrl 발급 성공"),
    })
    public CommonApiResponse<GetPreSignedUrlResponseDto> getPreSignedUrl(
        @RequestBody @Valid GetPreSignedUrlRequestDto request) {
        GetPreSignedUrlResponseDto response = s3Service.getPreSignedUrl(request);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }
}
