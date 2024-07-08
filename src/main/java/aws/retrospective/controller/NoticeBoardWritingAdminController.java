package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.common.CurrentUser;
import aws.retrospective.dto.NoticeBoardWritingRequestDto;
import aws.retrospective.dto.NoticeBoardWritingResponseDto;
import aws.retrospective.dto.GetPreSignedUrlRequestDto;
import aws.retrospective.dto.GetPreSignedUrlResponseDto;
import aws.retrospective.entity.User;
import aws.retrospective.exception.custom.ForbiddenAccessException;
import aws.retrospective.service.NoticeBoardWritingService;
import aws.retrospective.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notices")
@Tag(name = "Admin Notice Board Writing")
@SecurityRequirement(name = "JWT")
public class NoticeBoardWritingAdminController {

    private final NoticeBoardWritingService noticeBoardWritingService;
    private final S3Service s3Service;

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시글 작성 성공")})
    @PostMapping("/posts")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CommonApiResponse<NoticeBoardWritingResponseDto> createPost(@CurrentUser User user, @RequestBody @Valid NoticeBoardWritingRequestDto requestDto) {
        NoticeBoardWritingResponseDto responseDto = noticeBoardWritingService.savePost(requestDto);
        return CommonApiResponse.successResponse(HttpStatus.OK, responseDto);
    }

    @Operation(summary = "임시 게시글 작성", description = "임시 게시글을 저장하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "임시 게시글 저장 성공")})
    @PostMapping("/temp-posts")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CommonApiResponse<NoticeBoardWritingResponseDto> saveTempPost(@CurrentUser User user, @RequestBody @Valid NoticeBoardWritingRequestDto requestDto) {
        NoticeBoardWritingResponseDto responseDto = noticeBoardWritingService.saveTempPost(requestDto);
        return CommonApiResponse.successResponse(HttpStatus.OK, responseDto);
    }

    @Operation(summary = "Presigned URL 발급", description = "사진 및 파일 업로드를 위한 presigned URL을 발급하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Presigned URL 발급 성공")})
    @PostMapping("/files/presigned-url")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CommonApiResponse<GetPreSignedUrlResponseDto> getPresignedUrl(@CurrentUser User user, @RequestBody @Valid GetPreSignedUrlRequestDto request) {
        if (!user.isAdministrator()) {
            throw new ForbiddenAccessException("파일 업로드 권한이 없습니다.");
        }
        GetPreSignedUrlResponseDto response = s3Service.getPreSignedUrl(request);
        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "게시글 삭제 성공")})
    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deletePost(@CurrentUser User user, @PathVariable Long id) {
        noticeBoardWritingService.deletePost(id);
    }

    @Operation(summary = "게시글 수정", description = "기존 게시글을 수정하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시글 수정 성공")})
    @PutMapping("/posts/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CommonApiResponse<NoticeBoardWritingResponseDto> updatePost(@CurrentUser User user, @PathVariable Long id, @RequestBody @Valid NoticeBoardWritingRequestDto requestDto) {
        NoticeBoardWritingResponseDto responseDto = noticeBoardWritingService.updatePost(id, requestDto);
        return CommonApiResponse.successResponse(HttpStatus.OK, responseDto);
    }

}
