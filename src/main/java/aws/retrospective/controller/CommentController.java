package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.CommentDto;
import aws.retrospective.dto.CreateCommentDto;
import aws.retrospective.dto.UpdateCommentDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sections/{sectionId}/comments")
@Tag(name = "comments")
public class CommentController {

    private final CommentService commentService;

    // 모든 댓글 조회
    @Operation(summary = "Get all comments", responses = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved comments"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public CommonApiResponse<List<CommentDto>> getAllComments() {
        List<CommentDto> commentDtoList = commentService.getAllComments();
        return CommonApiResponse.successResponse(HttpStatus.OK, commentDtoList);
    }


    // 특정 댓글 조회
    @GetMapping("/sections/{sectionId}/comments")
    @Operation(summary = "Get comment by ID", responses = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved comment"),
        @ApiResponse(responseCode = "404", description = "Comment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public CommonApiResponse<CommentDto> getCommentById(@PathVariable Long id) {
        CommentDto commentDto = commentService.getCommentDTOById(id);
        return CommonApiResponse.successResponse(HttpStatus.OK, commentDto);
    }

    // 댓글 생성
    @Operation(summary = "Create a new comment", responses = {
        @ApiResponse(responseCode = "201", description = "Comment created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping()
    public CommonApiResponse<CreateCommentDto> createComment(
        @RequestBody CreateCommentDto createCommentDto) {
        Comment createdComment = commentService.createComment(createCommentDto);

        createCommentDto = new CreateCommentDto();

        return CommonApiResponse.successResponse(HttpStatus.CREATED, createCommentDto);
    }


    // 댓글 수정
    @Operation(summary = "Update an existing comment", responses = {
        @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
        @ApiResponse(responseCode = "404", description = "Comment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/sections/{sectionId}/comments")
    public CommonApiResponse<UpdateCommentDto> updateComment(
        @RequestBody UpdateCommentDto updateCommentDto) {
        Comment updatedComment = commentService.updateComment(updateCommentDto);

        UpdateCommentDto updatedCommentDto = new UpdateCommentDto();

        return CommonApiResponse.successResponse(HttpStatus.OK, updatedCommentDto);
    }


    // 댓글 삭제
    @Operation(summary = "Delete a comment by ID", responses = {
        @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Comment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommonApiResponse<Void> deleteComment(@PathVariable Long commentId) {
        return CommonApiResponse.successResponse(HttpStatus.NO_CONTENT, null);
    }
}