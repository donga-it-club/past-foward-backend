package aws.retrospective.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aws.retrospective.dto.CreateCommentDto;
import aws.retrospective.dto.CreateCommentResponseDto;
import aws.retrospective.dto.DeleteCommentRequestDto;
import aws.retrospective.dto.UpdateCommentRequestDto;
import aws.retrospective.dto.UpdateCommentResponseDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.User;
import aws.retrospective.repository.CommentRepository;
import aws.retrospective.repository.SectionRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("댓글 등록 API")
    void createCommentTest() {
        //given
        Long userId = 1L;
        User user = createUser();
        ReflectionTestUtils.setField(user, "id", userId);

        Long sectionId = 1L;
        Section section = createSection();
        ReflectionTestUtils.setField(section, "id", sectionId);

        Comment mockComment = createComment();
        when(sectionRepository.findById(sectionId)).thenReturn(Optional.of(section));
        when(commentRepository.save(any())).thenReturn(mockComment);

        CreateCommentDto request = new CreateCommentDto();
        ReflectionTestUtils.setField(request, "sectionId", sectionId);
        ReflectionTestUtils.setField(request, "commentContent", "test");

        //when
        CreateCommentResponseDto response = commentService.createComment(user, request);

        //then
        assertThat(response.getCommentContent()).isEqualTo("test");
        assertThat(response.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("댓글 삭제 성공 API")
    void deleteCommentSuccessTest() {
        //given
        User user = createUser();
        ReflectionTestUtils.setField(user, "id", 1L);
        Section section = createSection();

        Long commentId = 1L;
        Comment comment = createComment(user, section);
        ReflectionTestUtils.setField(comment, "id", commentId);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        //when
        commentService.deleteComment(commentId, user);

        //then
        verify(commentRepository).delete(comment);
    }

    @Test
    @DisplayName("존재하지 않는 댓글 삭제 시 예외 발생")
    void deleteCommentFailTest() {

        //given
        Long notExistCommentId = 1L;
        User user = createUser();

        DeleteCommentRequestDto request = new DeleteCommentRequestDto();
        ReflectionTestUtils.setField(request, "userId", 1L);

        //when
        when(commentRepository.findById(notExistCommentId)).thenThrow(NoSuchElementException.class);

        //then
        assertThrows(NoSuchElementException.class,
            () -> commentService.deleteComment(notExistCommentId, user));
    }

    private static Comment createComment(User user,
        Section section) {
        return Comment.builder().user(user).content("test")
            .section(section).build();
    }

    @Test
    @DisplayName("특정 댓글 내용 수정")
    void updateCommentContentTest() {

        //given
        Long userId = 1L;
        User longinedUser = createUser();
        ReflectionTestUtils.setField(longinedUser, "id", userId);

        Long commentId = 1L;
        Comment comment = createComment();
        ReflectionTestUtils.setField(comment, "id", commentId);
        ReflectionTestUtils.setField(comment, "user", longinedUser);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        //when
        UpdateCommentRequestDto request = new UpdateCommentRequestDto();
        ReflectionTestUtils.setField(request, "commentContent", request.getCommentContent());
        UpdateCommentResponseDto response = commentService.updateCommentContent(longinedUser,
            commentId, request);

        //then
        assertThat(response.getCommentId()).isEqualTo(commentId);
        assertThat(response.getContent()).isEqualTo(request.getCommentContent());
    }


    private Comment createComment() {
        return Comment.builder().content("test").build();
    }

    private Section createSection() {
        return Section.createSection("test", null, null, null);
    }

    private static User createUser() {
        return User.builder().username("test").phone("010-1234-1234").email("test@naver.com")
            .build();
    }
}