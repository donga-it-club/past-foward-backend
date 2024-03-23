package aws.retrospective.service;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.CommentDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public CommonApiResponse<List<CommentDto>> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        List<CommentDto> commentDtos = comments.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return CommonApiResponse.successResponse(HttpStatus.OK, commentDtos);
    }

    public CommonApiResponse<CommentDto> getCommentDTOById(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        CommentDto commentDto = convertToDTO(comment);
        return CommonApiResponse.successResponse(HttpStatus.OK, commentDto);
    }

    @Transactional(readOnly = true)
    public CommonApiResponse<Comment> getCommentById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.map(value -> CommonApiResponse.successResponse(HttpStatus.OK, value))
            .orElseGet(() -> CommonApiResponse.errorResponse(HttpStatus.NOT_FOUND, "Comment not found"));
    }

    @Transactional
    public CommonApiResponse<Comment> createComment(Comment comment) {
        Comment createdComment = commentRepository.save(comment);
        return CommonApiResponse.successResponse(HttpStatus.CREATED, createdComment);
    }

    @Transactional
    public CommonApiResponse<Comment> updateComment(Long id, Comment updatedComment) {
        Comment existingComment = commentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        existingComment.getUpdatedDate();
        Comment savedComment = commentRepository.save(existingComment);
        return CommonApiResponse.successResponse(HttpStatus.OK, savedComment);
    }

    @Transactional
    public CommonApiResponse<Void> deleteComment(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return CommonApiResponse.successResponse(HttpStatus.NO_CONTENT, null);
        }
        return CommonApiResponse.errorResponse(HttpStatus.BAD_REQUEST, "Failed to delete comment");
    }

    private CommentDto convertToDTO(Comment comment) {
        CommentDto commentDto = new CommentDto(comment.getId(), comment.getContent());
        // 필요한 다른 필드들도 엔티티에서 DTO로 복사합니다.

        return commentDto;
    }
}
