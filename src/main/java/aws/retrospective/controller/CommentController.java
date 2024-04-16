package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.CommentDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sections/{sectionId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 모든 댓글 조회
    @GetMapping
    @Operation(summary = "Get all comments", responses = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved comments"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CommonApiResponse<List<CommentDto>>> getAllComments() {
        int totalCount = commentService.getTotalCommentCount(); // 전체 댓글 수 조회
        List<CommentDto> commentDtoList = commentService.getAllComments(totalCount);
        return ResponseEntity.ok(CommonApiResponse.successResponse(HttpStatus.OK, commentDtoList));
    }


    // 특정 댓글 조회
    @GetMapping("/{id}")
    @Operation(summary = "Get comment by ID", responses = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved comment"),
        @ApiResponse(responseCode = "404", description = "Comment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CommonApiResponse<CommentDto>> getCommentById(@PathVariable Long id) {
        int totalCount = commentService.getTotalCommentCount(); // 전체 댓글 수 조회
        CommentDto commentDto = commentService.getCommentDTOById(id, totalCount);
        return ResponseEntity.ok(CommonApiResponse.successResponse(HttpStatus.OK, commentDto));
    }

    // 댓글 생성
    @PostMapping
    @Operation(summary = "Create a new comment", responses = {
        @ApiResponse(responseCode = "201", description = "Comment created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CommonApiResponse<Comment>> createComment(@RequestBody Comment comment) {
        Comment createdComment = commentService.createComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CommonApiResponse.successResponse(HttpStatus.CREATED, createdComment));
    }

    // 댓글 업데이트
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing comment", responses = {
        @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
        @ApiResponse(responseCode = "404", description = "Comment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CommonApiResponse<Comment>> updateComment(@PathVariable Long id, @RequestBody Comment updatedComment) {
        Comment updated = commentService.updateComment(id, updatedComment);
        return ResponseEntity.ok(CommonApiResponse.successResponse(HttpStatus.OK, updated));
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a comment by ID", responses = {
        @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Comment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CommonApiResponse<Void>> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(CommonApiResponse.successResponse(HttpStatus.NO_CONTENT, null));
    }
}
