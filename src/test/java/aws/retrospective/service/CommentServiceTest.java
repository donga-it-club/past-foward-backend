import aws.retrospective.entity.Comment;
import aws.retrospective.entity.User;
import aws.retrospective.entity.Section;
import aws.retrospective.repository.CommentRepository;
import aws.retrospective.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
        Comment comment = Comment.builder()
            .content("Sample content")
            .user(User.builder().build())
            .section(Section.builder().build())
            .build();
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
        Comment comment = Comment.builder()
            .content("Sample content")
            .user(User.builder().build())
            .section(Section.builder().build())
            .build();
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment result = commentService.createComment(comment);

        assertEquals(comment, result);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void updateComment() {
        Long commentId = 1L;
        Comment existingComment = Comment.builder()
            .content("Sample content")
            .user(User.builder().build())
            .section(Section.builder().build())
            .build();
        Comment updatedComment = Comment.builder()
            .content("Updated content")
            .user(User.builder().build())
            .section(Section.builder().build())
            .build();
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
        Comment updatedComment = Comment.builder()
            .content("Updated content")
            .user(User.builder().build())
            .section(Section.builder().build())
            .build();
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
