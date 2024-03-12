import aws.retrospective.entity.Comment;
import aws.retrospective.repository.CommentRepository;
import aws.retrospective.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void getAllComments() {
        List<Comment> comments = new ArrayList<>();
        when(commentRepository.findAll()).thenReturn(comments);

        List<Comment> result = commentService.getAllComments();

        assertEquals(comments, result);
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void getCommentById() {
        Long commentId = 1L;
        Comment comment = new Comment();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        Optional<Comment> result = commentService.getCommentId(commentId);

        assertTrue(result.isPresent());
        assertEquals(comment, result.get());
        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void getCommentById_NotFound() {
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        Optional<Comment> result = commentService.getCommentId(commentId);

        assertFalse(result.isPresent());
        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void createComment() {
        Comment comment = new Comment();
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment result = commentService.createComment(comment);

        assertEquals(comment, result);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void updateComment() {
        Long commentId = 1L;
        Comment existingComment = new Comment();
        Comment updatedComment = new Comment();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(existingComment)).thenReturn(updatedComment);

        Comment result = commentService.updateComment(commentId, updatedComment);

        assertEquals(updatedComment, result);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).save(existingComment);
    }

    @Test
    void updateComment_NotFound() {
        Long commentId = 1L;
        Comment updatedComment = new Comment();
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        Comment result = commentService.updateComment(commentId, updatedComment);

        assertNull(result);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).save(any());
    }

    @Test
    void deleteComment() {
        Long commentId = 1L;

        commentService.deleteComment(commentId);

        verify(commentRepository, times(1)).deleteById(commentId);
    }
}
