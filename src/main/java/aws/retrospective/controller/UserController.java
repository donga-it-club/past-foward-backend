package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.GetPreSignedUrlRequestDto;
import aws.retrospective.dto.GetPreSignedUrlResponseDto;
import aws.retrospective.dto.UpdateUserProfileRequestDto;
import aws.retrospective.dto.UpdateUserProfileResponseDto;
import aws.retrospective.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // PreSignedUrl 발급
    @PostMapping("/pre-signed-url")
    public CommonApiResponse<GetPreSignedUrlResponseDto> getPreSignedUrl(
        @RequestBody @Valid GetPreSignedUrlRequestDto request) {
        GetPreSignedUrlResponseDto response = userService.getPreSignedUrl(request);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    // 프로필 이미지 등록
    @PutMapping("/{userId}/thumbnail")
    public CommonApiResponse<UpdateUserProfileResponseDto> updateProfile(@PathVariable Long userId,
        @RequestBody @Valid UpdateUserProfileRequestDto request) {
        UpdateUserProfileResponseDto response = userService.updateProfile(
            userId, request);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

}
