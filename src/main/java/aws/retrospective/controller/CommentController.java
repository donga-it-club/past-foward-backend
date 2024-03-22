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
        CommonApiResponse<List<CommentDto>> response = commentService.getAllComments();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Comment>> getCommentById(@PathVariable Long id) {
        CommonApiResponse<Comment> response = commentService.getCommentById(id);
        return response.getData() != null
            ? ResponseEntity.ok(response)
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PostMapping
    public ResponseEntity<CommonApiResponse<Comment>> createComment(@RequestBody Comment comment) {
        CommonApiResponse<Comment> response = commentService.createComment(comment);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Comment>> updateComment(@PathVariable Long id, @RequestBody Comment updatedComment) {
        CommonApiResponse<Comment> response = commentService.updateComment(id, updatedComment);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> deleteComment(@PathVariable Long id) {
        CommonApiResponse<Void> response = commentService.deleteComment(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
