package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.UpdateUserProfileRequestDto;
import aws.retrospective.dto.UpdateUserProfileResponseDto;
import aws.retrospective.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    // 프로필 이미지 등록
    @PutMapping("/{userId}/thumbnail")
    @Operation(summary = "프로필 이미지 등록")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "프로필 이미지 등록 성공"),
    })
    public CommonApiResponse<UpdateUserProfileResponseDto> updateProfile(@PathVariable Long userId,
        @RequestBody @Valid UpdateUserProfileRequestDto request) {
        UpdateUserProfileResponseDto response = userService.updateProfile(
            userId, request);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }
}
