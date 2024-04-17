package aws.retrospective.service;

import aws.retrospective.dto.CommentDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<CommentDto> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommentDto getCommentDTOById(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        return convertToDTO(comment);
    }

    @Transactional
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Long id, Comment updatedComment) {
        Comment existingComment = commentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        existingComment.updateContent(updatedComment.getContent());
        return existingComment;
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment commentToDelete = findCommentById(id);
        commentRepository.delete(commentToDelete);
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
    }


    private CommentDto convertToDTO(Comment comment) {
        return new CommentDto(comment.getId(), comment.getContent());
        // 필요한 다른 필드들도 엔티티에서 DTO로 복사합니다.
    }

}