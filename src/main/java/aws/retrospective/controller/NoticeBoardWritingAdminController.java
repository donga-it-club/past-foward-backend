package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.common.CurrentUser;
import aws.retrospective.dto.NoticeBoardWritingRequestDto;
import aws.retrospective.dto.NoticeBoardWritingResponseDto;
import aws.retrospective.entity.User;
import aws.retrospective.exception.custom.ForbiddenAccessException;
import aws.retrospective.service.NoticeBoardWritingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/noticeboardwriting")
@Tag(name = "Admin Notice Board Writing")
@SecurityRequirement(name = "JWT")
public class NoticeBoardWritingAdminController {

    private final NoticeBoardWritingService noticeBoardWritingService;

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시글 작성 성공")})
    @PostMapping("/create")
    public CommonApiResponse<NoticeBoardWritingResponseDto> createPost(@CurrentUser User user, @RequestBody @Valid NoticeBoardWritingRequestDto requestDto) {
        if (!user.isAdministrator()) {
            throw new ForbiddenAccessException("글쓰기 권한이 없습니다.");
        }
        NoticeBoardWritingResponseDto responseDto = noticeBoardWritingService.savePost(requestDto);
        return CommonApiResponse.successResponse(HttpStatus.OK, responseDto);
    }

    @Operation(summary = "임시 게시글 저장", description = "임시 게시글을 저장하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "임시 게시글 저장 성공")})
    @PostMapping("/save-temp")
    public CommonApiResponse<NoticeBoardWritingResponseDto> saveTempPost(@CurrentUser User user, @RequestBody @Valid NoticeBoardWritingRequestDto requestDto) {
        if (!user.isAdministrator()) {
            throw new ForbiddenAccessException("글쓰기 권한이 없습니다.");
        }
        NoticeBoardWritingResponseDto responseDto = noticeBoardWritingService.saveTempPost(requestDto);
        return CommonApiResponse.successResponse(HttpStatus.OK, responseDto);
    }

    @Operation(summary = "파일 업로드", description = "파일을 업로드하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "파일 업로드 성공")})
    @PostMapping("/upload")
    public CommonApiResponse<String> uploadFile(@CurrentUser User user, @RequestParam("file") MultipartFile file) {
        if (!user.isAdministrator()) {
            throw new ForbiddenAccessException("파일 업로드 권한이 없습니다.");
        }
        try {
            String fileUrl = noticeBoardWritingService.uploadFile(file);
            return CommonApiResponse.successResponse(HttpStatus.OK, fileUrl);
        } catch (IOException e) {
            return CommonApiResponse.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed");
        }
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "게시글 삭제 성공")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@CurrentUser User user, @PathVariable Long id) {
        if (!user.isAdministrator()) {
            throw new ForbiddenAccessException("삭제 권한이 없습니다.");
        }
        noticeBoardWritingService.deletePost(id);
    }
}
