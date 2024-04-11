package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.CommentDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<CommonApiResponse<List<CommentDto>>> getAllComments() {
        List<CommentDto> commentDtoList = commentService.getAllComments();
        return ResponseEntity.ok(CommonApiResponse.successResponse(HttpStatus.OK, commentDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<CommentDto>> getCommentById(@PathVariable Long id) {
        CommentDto commentDto = commentService.getCommentDTOById(id);
        return ResponseEntity.ok(CommonApiResponse.successResponse(HttpStatus.OK, commentDto));
    }

    @PostMapping
    public ResponseEntity<CommonApiResponse<Comment>> createComment(@RequestBody Comment comment) {
        Comment createdComment = commentService.createComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CommonApiResponse.successResponse(HttpStatus.CREATED, createdComment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Comment>> updateComment(@PathVariable Long id, @RequestBody Comment updatedComment) {
        Comment updated = commentService.updateComment(id, updatedComment);
        return ResponseEntity.ok(CommonApiResponse.successResponse(HttpStatus.OK, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(CommonApiResponse.successResponse(HttpStatus.NO_CONTENT, null));
    }
}
