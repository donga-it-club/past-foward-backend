package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.common.CurrentUser;
import aws.retrospective.dto.CreateCommentDto;
import aws.retrospective.dto.CreateCommentResponseDto;
import aws.retrospective.dto.UpdateCommentRequestDto;
import aws.retrospective.dto.UpdateCommentResponseDto;
import aws.retrospective.entity.User;
import aws.retrospective.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@Tag(name = "comments")
@SecurityRequirement(name = "JWT")
public class CommentController {

    private final CommentService commentService;


    // 댓글 생성
    @Operation(summary = "Create a new comment", responses = {
        @ApiResponse(responseCode = "201", description = "Comment created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CommonApiResponse<CreateCommentResponseDto> createComment(@CurrentUser User user,
        @Valid @RequestBody CreateCommentDto request) {
        CreateCommentResponseDto response = commentService.createComment(user, request);

        return CommonApiResponse.successResponse(HttpStatus.CREATED, response);
    }


    // 댓글 수정
    @Operation(summary = "Update an existing comment", responses = {
        @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
        @ApiResponse(responseCode = "404", description = "Comment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{commentId}")
    public CommonApiResponse<UpdateCommentResponseDto> updateCommentContent(@CurrentUser User user,
        @PathVariable Long commentId, @Valid @RequestBody UpdateCommentRequestDto request) {
        UpdateCommentResponseDto response = commentService.updateCommentContent(user, commentId,
            request);

        return CommonApiResponse.successResponse(HttpStatus.OK, response);
    }


    // 댓글 삭제
    @Operation(summary = "Delete a comment by ID", responses = {
        @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Comment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@CurrentUser User user,
        @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId, user);

    }
}