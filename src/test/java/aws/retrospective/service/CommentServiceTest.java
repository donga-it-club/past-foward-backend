package aws.retrospective.service;

import aws.retrospective.dto.CommentDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.User;
import aws.retrospective.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
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

        List<CommentDto> result = commentService.getAllComments();

        assertEquals(comments.size(), result.size());
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

        Optional<Comment> result = commentService.getCommentById(commentId);

        assertTrue(result.isPresent());
        assertEquals(comment, result.get());
        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void getCommentById_NotFound() {
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        Optional<Comment> result = commentService.getCommentById(commentId);

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
            .id(commentId) // Set ID to existing comment
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

        // Check if EntityNotFoundException is thrown when updating a non-existent comment
        assertThrows(EntityNotFoundException.class, () -> commentService.updateComment(commentId, updatedComment));

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).save(any());
    }

    @Test
    void deleteComment() {
        Long commentId = 1L;

        when(commentRepository.existsById(commentId)).thenReturn(true);

        commentService.deleteComment(commentId);

        verify(commentRepository, times(1)).existsById(commentId);
        verify(commentRepository, times(1)).deleteById(commentId);
    }

    @Test
    void deleteComment_NotFound() {
        Long commentId = 1L;

        when(commentRepository.existsById(commentId)).thenReturn(false);

        boolean result = commentService.deleteComment(commentId);

        assertFalse(result);
        verify(commentRepository, times(1)).existsById(commentId);
        verify(commentRepository, never()).deleteById(commentId);
    }
}
