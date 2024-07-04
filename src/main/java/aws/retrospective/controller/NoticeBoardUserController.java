package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.NoticeBoardListDto;
import aws.retrospective.dto.NoticeBoardWritingResponseDto;
import aws.retrospective.dto.PagedResponseDto;
import aws.retrospective.service.NoticeBoardWritingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "Notice Board Writing")
public class NoticeBoardUserController {

    private final NoticeBoardWritingService noticeBoardWritingService;

    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 조회하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공")})
    @GetMapping
    public CommonApiResponse<PagedResponseDto<NoticeBoardListDto>> getAllPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponseDto<NoticeBoardListDto> responseDtoList = noticeBoardWritingService.getAllPosts(page, size);
        return CommonApiResponse.successResponse(HttpStatus.OK, responseDtoList);
    }

    @Operation(summary = "개별 게시글 조회", description = "개별 게시글을 조회하는 API")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "개별 게시글 조회 성공")})
    @GetMapping("/{id}")
    public CommonApiResponse<NoticeBoardWritingResponseDto> getPostById(@PathVariable Long id) {
        NoticeBoardWritingResponseDto responseDto = noticeBoardWritingService.getPostById(id);
        return CommonApiResponse.successResponse(HttpStatus.OK, responseDto);
    }
}