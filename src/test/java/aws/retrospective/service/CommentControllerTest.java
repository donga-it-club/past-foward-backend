package aws.retrospective.service;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.controller.CommentController;
import aws.retrospective.dto.CommentDto;
import aws.retrospective.entity.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllComments() {
        // Arrange
        List<CommentDto> commentDtoList = new ArrayList<>();
        when(commentService.getAllComments()).thenReturn(commentDtoList);

        // Act
        ResponseEntity<CommonApiResponse<List<CommentDto>>> responseEntity = commentController.getAllComments();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(commentDtoList, responseEntity.getBody().getData());
        verify(commentService, times(1)).getAllComments();
    }

    @Test
    void getCommentById() {
        // Arrange
        Long commentId = 1L;
        CommentDto commentDto = new CommentDto(commentId, "Sample content");
        when(commentService.getCommentDTOById(commentId)).thenReturn(commentDto);

        // Act
        ResponseEntity<CommonApiResponse<CommentDto>> responseEntity = commentController.getCommentById(commentId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(commentDto, responseEntity.getBody().getData());
        verify(commentService, times(1)).getCommentDTOById(commentId);
    }

    @Test
    void createComment() {
        // Arrange
        Comment comment = new Comment("Sample content", null, null); // Proper user and section should be passed
        when(commentService.createComment(comment)).thenReturn(comment);

        // Act
        ResponseEntity<CommonApiResponse<Comment>> responseEntity = commentController.createComment(comment);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(comment, responseEntity.getBody().getData());
        verify(commentService, times(1)).createComment(comment);
    }

    @Test
    void updateComment() {
        // Arrange
        Long commentId = 1L;
        Comment updatedComment = new Comment("Updated content", null, null); // Proper user and section should be passed
        when(commentService.updateComment(commentId, updatedComment)).thenReturn(updatedComment);

        // Act
        ResponseEntity<CommonApiResponse<Comment>> responseEntity = commentController.updateComment(commentId, updatedComment);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedComment, responseEntity.getBody().getData());
        verify(commentService, times(1)).updateComment(commentId, updatedComment);
    }

    @Test
    void deleteComment() {
        // Arrange
        Long commentId = 1L;

        // Act
        ResponseEntity<CommonApiResponse<Void>> responseEntity = commentController.deleteComment(commentId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody().getData());
        verify(commentService, times(1)).deleteComment(commentId);
    }
}
