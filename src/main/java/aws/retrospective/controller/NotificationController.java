package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.SectionNotificationDto;
import aws.retrospective.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "새로운 댓글 및 좋아요 조회", description = "새로운 댓글과 좋아요를 조회하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 알림이 조회되었습니다.")
    })
    @GetMapping
    public CommonApiResponse<List<SectionNotificationDto>> getNewCommentsAndLikes() {
        List<SectionNotificationDto> result = notificationService.getNewCommentsAndLikes();
        return CommonApiResponse.successResponse(HttpStatus.OK, result);
    }

}
