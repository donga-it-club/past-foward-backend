package aws.retrospective.service;

import aws.retrospective.dto.CommentDto;
import aws.retrospective.dto.CreateCommentDto;
import aws.retrospective.dto.UpdateCommentDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.User;
import aws.retrospective.repository.CommentRepository;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static aws.retrospective.util.TestUtil.createSection;
import static aws.retrospective.util.TestUtil.createUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 모든 댓글 조회
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
        assertEquals("Comment 1", commentDtos.get(0).getContent()); // 확인을 위해 첫 번째 댓글 내용 확인
        assertEquals("Comment 2", commentDtos.get(1).getContent()); // 확인을 위해 두 번째 댓글 내용 확인
        verify(commentRepository, times(1)).findAll();
    }

    // 특정 댓글 조회
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

    // 댓글 생성
    @Test
    void createComment() {
        // Arrange
        // CreateCommentDto에 사용자 ID와 섹션 ID 설정
        CreateCommentDto createCommentDto = new CreateCommentDto(1L, 2L, "New comment"); // 사용자 ID, 섹션 ID, 댓글 내용 설정

        // 댓글 생성
        Comment comment = Comment.builder()
            .id(1L)
            .content("New comment")
            .build();

        // UserRepository에서 사용자 조회시 가짜 사용자 반환
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().build()));
        // SectionRepository에서 섹션 조회시 가짜 섹션 반환
        when(sectionRepository.findById(anyLong())).thenReturn(Optional.of(Section.builder().build()));
        // CommentRepository에서 댓글 저장시 생성된 댓글 반환
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        Comment createdComment = commentService.createComment(createCommentDto);

        // Assert
        assertNotNull(createdComment);
        assertNotNull(createdComment.getId());
        assertEquals("New comment", createdComment.getContent());
    }

    @Test
    void createComment_Failure() {
        // Arrange
        // CreateCommentDto에 사용자 ID와 섹션 ID 설정
        CreateCommentDto createCommentDto = new CreateCommentDto(1L, 2L, "New comment"); // 사용자 ID, 섹션 ID, 댓글 내용 설정

        // UserRepository에서 사용자 조회시 사용자가 존재하지 않음을 반환
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        // SectionRepository에서 섹션 조회시 섹션이 존재하지 않음을 반환
        when(sectionRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        // 사용자나 섹션이 존재하지 않는 경우 IllegalArgumentException이 발생해야 함
        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(createCommentDto));
    }


    // 댓글 수정
    @Test
    void updateComment() {
        //given
        Long userId = 1L;
        User loginedUser = createUser();
        ReflectionTestUtils.setField(loginedUser, "id", userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(loginedUser));

        Long sectionId = 1L;
        Section section = createSection(loginedUser);
        ReflectionTestUtils.setField(section, "id", sectionId);
        when(sectionRepository.findById(sectionId)).thenReturn(Optional.of(section));

        Long commentId = 1L;
        UpdateCommentDto updateCommentDto = new UpdateCommentDto(commentId, "Updated content"); // Proper data should be provided
        Comment updatedComment = new Comment(1L, "Updated content", null, null); // Proper user and section should be passed
        when(commentService.updateComment(updateCommentDto)).thenReturn(updatedComment);

        // 사용자와 섹션 생성 및 설정
        User existingUser = createUser();
        Section existingSection = createSection(existingUser);

        // 기존 댓글 객체 생성
        Comment existingComment = Comment.builder()
            .id(commentId)
            .content(existingContent)
            .user(existingUser)
            .section(existingSection)
            .build();

        // UserRepository에서 사용자 조회시 가짜 사용자 반환
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        // SectionRepository에서 섹션 조회시 가짜 섹션 반환
        when(sectionRepository.findById(anyLong())).thenReturn(Optional.of(existingSection));
        // commentRepository에서 기존 댓글 조회시 해당 댓글 반환
        when(commentRepository.findById(eq(commentId))).thenReturn(Optional.of(existingComment));
        // commentRepository에서 업데이트된 댓글 반환
        when(commentRepository.save(existingComment)).thenReturn(existingComment);

        // UpdateCommentDto 객체 생성
        UpdateCommentDto updateCommentDto = new UpdateCommentDto(commentId, existingUser.getId(), existingSection.getId(), updatedContent);

        // Act
        commentService.updateComment(updateCommentDto);

        // Assert
        assertEquals(updatedContent, existingComment.getContent());
    }



    @Test
    void updateComment_Failure() {
        // Arrange
        Long id = 1L;
        String existingContent = "Existing comment";
        String updatedContent = "Updated comment";
        Comment existingComment = Comment.builder().id(id).content(existingContent).build();
        UpdateCommentDto updateCommentDto = new UpdateCommentDto(); // 적절한 데이터를 제공해야 함

        // UserRepository에서 사용자 조회시 사용자가 존재하지 않음을 반환
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        // SectionRepository에서 섹션 조회시 섹션이 존재하지 않음을 반환
        when(sectionRepository.findById(anyLong())).thenReturn(Optional.empty());
        // commentRepository에서 기존 댓글 조회시 해당 댓글 반환
        when(commentRepository.findById(id)).thenReturn(Optional.of(existingComment));

        // Act & Assert
        // 사용자나 섹션이 존재하지 않는 경우 IllegalArgumentException이 발생해야 함
        assertThrows(IllegalArgumentException.class, () -> commentService.updateComment(updateCommentDto));
    }

    // 댓글 삭제
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
        verify(commentRepository, never()).delete((Comment) any());
    }

}