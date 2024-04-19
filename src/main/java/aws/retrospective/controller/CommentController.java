package aws.retrospective.controller;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.CommentDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
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
    public ResponseEntity<CommonApiResponse<Comment>> updateComment(@PathVariable Long id,
        @RequestBody Comment updatedComment) {
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
