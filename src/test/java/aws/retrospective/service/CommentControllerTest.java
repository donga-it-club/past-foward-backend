package aws.retrospective.service;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.controller.CommentController;
import aws.retrospective.dto.CommentDto;
import aws.retrospective.dto.CreateCommentDto;
import aws.retrospective.dto.UpdateCommentDto;
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

        // Act
        CommonApiResponse<List<CommentDto>> response = commentController.getAllComments();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals(commentDtoList, response.getData());
        verify(commentService, times(1)).getAllComments();
    }

    @Test
    void getCommentById() {
        // Arrange
        Long commentId = 1L;
        CommentDto expectedCommentDto = new CommentDto(commentId, "Sample content");
        when(commentService.getCommentDTOById(commentId)).thenReturn(expectedCommentDto);

        // Act
        CommonApiResponse<CommentDto> response = commentController.getCommentById(commentId);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertNotNull(response.getData());
        assertEquals(expectedCommentDto, response.getData());
        verify(commentService, times(1)).getCommentDTOById(commentId);
    }

    @Test
    void createComment() {
        // Arrange
        CreateCommentDto createCommentDto = new CreateCommentDto(1L, 2L, "New comment");
        Comment createdComment = new Comment(1L, createCommentDto.getCommentContent(), null, null, null, null);
        when(commentService.createComment(createCommentDto)).thenReturn(createdComment);

        // Act
        CommonApiResponse<CreateCommentDto> response = commentController.createComment(createCommentDto);

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getCode());
        assertNotNull(response.getData());
        assertEquals(createdComment.getId(), response.getData().getCommentSectionId()); // 이 부분은 생성된 댓글의 ID를 확인하도록 수정해야 함
        verify(commentService, times(1)).createComment(createCommentDto);
    }


    @Test
    void updateComment() {
        // Arrange
        Long commentId = 1L;
        UpdateCommentDto updateCommentDto = new UpdateCommentDto(); // Proper data should be provided
        Comment updatedComment = new Comment(1L, "Updated content", null, null, null, null); // Proper user and section should be passed
        when(commentService.updateComment(updateCommentDto)).thenReturn(updatedComment);

        // Act
        CommonApiResponse<UpdateCommentDto> response = commentController.updateComment(updateCommentDto);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertNotNull(response.getData());
        assertEquals(updateCommentDto, response.getData());
        verify(commentService, times(1)).updateComment(updateCommentDto);
    }

    @Test
    void deleteComment() {
        // Arrange
        Long commentId = 1L;

        // Act
        CommonApiResponse<Void> response = commentController.deleteComment(commentId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getCode());
        assertNull(response.getData());
        verify(commentService, times(1)).deleteComment(commentId);
    }
}