package aws.retrospective.service;

import aws.retrospective.dto.CommentDto;
import aws.retrospective.entity.Comment;
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

class CommentServiceTest {

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
        // Arrange
        List<Comment> comments = new ArrayList<>();
        comments.add(Comment.builder().content("Comment 1").build());
        comments.add(Comment.builder().content("Comment 2").build());
        when(commentRepository.findAll()).thenReturn(comments);

        // Act
        List<CommentDto> commentDtos = commentService.getAllComments();

        // Assert
        assertEquals(comments.size(), commentDtos.size());
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void getCommentDTOById() {
        // Arrange
        Long id = 1L;
        String content = "Sample comment";
        Comment comment = Comment.builder().id(id).content(content).build();
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));

        // Act
        CommentDto commentDto = commentService.getCommentDTOById(id);

        // Assert
        assertNotNull(commentDto);
        assertEquals(id, commentDto.getId());
        assertEquals(content, commentDto.getContent());
        verify(commentRepository, times(1)).findById(id);
    }


    @Test
    void getCommentDTOById_NotFound() {
        // Arrange
        Long id = 1L;
        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> commentService.getCommentDTOById(id));
        verify(commentRepository, times(1)).findById(id);
    }

    @Test
    void createComment() {
        // Arrange
        Comment comment = Comment.builder().content("New comment").build();
        Comment savedComment = Comment.builder().id(1L).content("New comment").build(); // 예시로 ID를 1L로 설정
        when(commentRepository.save(comment)).thenReturn(savedComment);

        // Act
        Comment createdComment = commentService.createComment(comment);

        // Assert
        assertNotNull(createdComment.getId());
        assertEquals(1L, createdComment.getId()); // 예시로 ID가 1L인지 확인
        assertEquals("New comment", createdComment.getContent());
        verify(commentRepository, times(1)).save(comment);
    }


    @Test
    void updateComment() {
        // Arrange
        Long id = 1L;
        Comment existingComment = Comment.builder().id(id).content("Existing comment").build();
        Comment updatedComment = Comment.builder().id(id).content("Updated comment").build();
        when(commentRepository.findById(id)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(existingComment)).thenReturn(updatedComment);

        // Act
        Comment savedComment = commentService.updateComment(id, updatedComment);

        // Assert
        assertEquals("Updated comment", savedComment.getContent());
        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository, times(1)).save(existingComment);
    }

    @Test
    void updateComment_NotFound() {
        // Arrange
        Long id = 1L;
        Comment updatedComment = Comment.builder().id(id).content("Updated comment").build();
        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> commentService.updateComment(id, updatedComment));
        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository, never()).save(any());
    }

    @Test
    void deleteComment() {
        // Arrange
        Long id = 1L;
        Comment commentToDelete = Comment.builder().id(id).content("Comment to delete").build();
        when(commentRepository.findById(id)).thenReturn(Optional.of(commentToDelete));

        // Act
        commentService.deleteComment(id);

        // Assert
        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository, times(1)).delete(commentToDelete);
    }

    @Test
    void deleteComment_NotFound() {
        // Arrange
        Long id = 1L;
        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> commentService.deleteComment(id));
        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository, never()).delete(any());
    }
}
