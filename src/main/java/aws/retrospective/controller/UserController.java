package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.common.CurrentUser;
import aws.retrospective.dto.AdminRoleDTO;
import aws.retrospective.dto.GetUserInfoDto;
import aws.retrospective.dto.UpdateUserProfileRequestDto;
import aws.retrospective.dto.UpdateUserProfileResponseDto;
import aws.retrospective.entity.User;
import aws.retrospective.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User")
@SecurityRequirement(name = "JWT")
public class UserController {

    private final UserService userService;

    // 프로필 이미지 등록
    @PutMapping("/me")
    @Operation(summary = "유저 정보 수정")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "프로필 이미지 등록 성공"),})
    public CommonApiResponse<UpdateUserProfileResponseDto> updateProfile(@CurrentUser User user,
        @RequestBody @Valid UpdateUserProfileRequestDto request) {
        UpdateUserProfileResponseDto response = userService.updateProfile(user, request);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "유저 정보 조회")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "유저 정보 조회 성공"),})
    public CommonApiResponse<GetUserInfoDto> getUserInfo(@CurrentUser User user) {
        GetUserInfoDto response = userService.getUserInfo(user);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    // 관리자 권한 설정
    @PostMapping("/me/admin-status")
    @Operation(summary = "관리자 권한 설정")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "관리자 권한 설정 성공"),})
    public CommonApiResponse<Void> updateAdminStatus(@CurrentUser User user, @RequestBody @Valid AdminRoleDTO adminRoleDTO) {
        userService.updateAdminStatus(user, adminRoleDTO);
        return CommonApiResponse.successResponse(HttpStatus.OK, null);
    }
}
