package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.CommentDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<CommonApiResponse<List<CommentDto>>> getAllComments() {
        List<CommentDto> comments = commentService.getAllComments();
        return ResponseEntity.ok(CommonApiResponse.successResponse(HttpStatus.OK, comments));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Comment>> getCommentById(@PathVariable Long id) {
        Optional<Comment> comment = commentService.getCommentById(id);
        return comment.map(value -> ResponseEntity.ok(CommonApiResponse.successResponse(HttpStatus.OK, value)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonApiResponse.errorResponse(HttpStatus.NOT_FOUND, "Comment not found")));
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
        return updated != null ? ResponseEntity.ok(CommonApiResponse.successResponse(HttpStatus.OK, updated))
            : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonApiResponse.errorResponse(HttpStatus.NOT_FOUND, "Comment not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> deleteComment(@PathVariable Long id) {
        boolean deleted = commentService.deleteComment(id);
        return deleted ? ResponseEntity.noContent().build()
            : ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonApiResponse.errorResponse(HttpStatus.BAD_REQUEST, "Failed to delete comment"));
    }
}
