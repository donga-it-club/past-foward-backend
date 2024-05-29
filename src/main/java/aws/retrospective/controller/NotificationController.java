package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.GetNotificationResponseDto;
import aws.retrospective.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 조회", description = "알림 조회 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping()
    public CommonApiResponse<List<GetNotificationResponseDto>> getNewNotifications() {
        List<GetNotificationResponseDto> notifications = notificationService.getNotifications();
        return CommonApiResponse.successResponse(HttpStatus.OK, notifications);
    }

    @Operation(summary = "알림 읽기", description = "알림 읽음 처리 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @PostMapping("/{notificationId}")
    public CommonApiResponse<Void> readNotification(@PathVariable Long notificationId) {
        notificationService.readNotification(notificationId);
        return CommonApiResponse.successResponse(HttpStatus.OK);
    }
}
