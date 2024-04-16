package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.SendMailRequestDto;
import aws.retrospective.service.AmazonMailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/mails")
@Tag(name = "Mail", description = "메일 전송 API<br>"
    + "<b>SITE</b> : 사이트 문의<br>"
    + "<b>CREATOR</b> : 제작자 문의<br>"
    + "<b>ERROR</b> : 오류 문의<br>"
    + "<b>OTHER</b> : 기타")
public class MailController {

    private final AmazonMailService mailService;

    @PostMapping
    @Operation(summary = "메일 전송 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메일 전송 성공")
    })
    public CommonApiResponse<?> sendMail(@Valid @RequestBody SendMailRequestDto request) {
        mailService.sendMail(request);
        return CommonApiResponse.successResponse(HttpStatus.OK, null);
    }
}
