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
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<CommentDto>> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentDTOById(id));
    }

    @PostMapping
    public ResponseEntity<CommonApiResponse<Comment>> createComment(@RequestBody Comment comment) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(commentService.createComment(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Comment>> updateComment(@PathVariable Long id, @RequestBody Comment updatedComment) {
        return ResponseEntity.ok(commentService.updateComment(id, updatedComment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> deleteComment(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(commentService.deleteComment(id));
    }
}
