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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
        int totalCount = 10;
        when(commentService.getTotalCommentCount()).thenReturn(totalCount);
        when(commentService.getAllComments(totalCount)).thenReturn(commentDtoList);

        // Act
        CommonApiResponse<List<CommentDto>> responseEntity = commentController.getAllComments();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(commentDtoList, responseEntity.getBody().getData());
        verify(commentService, times(1)).getTotalCommentCount();
    }

    @Test
    void getCommentById() {
        // Arrange
        Long commentId = 1L;
        CommentDto commentDto = new CommentDto(1L, "Sample content", 10);
        int totalCount = 10;
        when(commentService.getTotalCommentCount()).thenReturn(totalCount);
        when(commentService.getCommentDTOById(commentId, totalCount)).thenReturn(commentDto);

        // Act
        CommonApiResponse<CommentDto> responseEntity = commentController.getCommentById(commentId);

        // Assert
        assertNotNull(responseEntity.getBody());
        assertEquals(commentDto, responseEntity.getBody().getData());
        verify(commentService, times(1)).getTotalCommentCount();
        verify(commentService, times(1)).getCommentDTOById(commentId, totalCount);
    }

    @Test
    void createComment() {
        // Arrange
        Comment comment = new Comment(1L, "Sample content", null, null); // Proper user and section should be passed
        when(commentService.createComment(comment)).thenReturn(comment);

        // Act
        CommonApiResponse<Comment> responseEntity = commentController.createComment(comment);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(comment, responseEntity.getBody().getData());
        verify(commentService, times(1)).createComment(comment);
    }

    @Test
    void updateComment() {
        // Arrange
        Long commentId = 1L;
        Comment updatedComment = new Comment(1L, "Updated content", null, null); // Proper user and section should be passed
        when(commentService.updateComment(commentId, updatedComment)).thenReturn(updatedComment);

        // Act
        CommonApiResponse<Comment> responseEntity = commentController.updateComment(commentId, updatedComment);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(updatedComment, responseEntity.getBody().getData());
        verify(commentService, times(1)).updateComment(commentId, updatedComment);
    }

    @Test
    void deleteComment() {
        // Arrange
        Long commentId = 1L;

        // Act
        CommonApiResponse<Void> responseEntity = commentController.deleteComment(commentId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertNull(responseEntity.getBody().getData()); // Assuming CommonApiResponse has a getData() method
        verify(commentService, times(1)).deleteComment(commentId);
    }

}
