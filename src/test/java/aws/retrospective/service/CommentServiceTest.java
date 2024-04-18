package aws.retrospective.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aws.retrospective.dto.CreateCommentDto;
import aws.retrospective.dto.CreateCommentResponseDto;
import aws.retrospective.dto.DeleteCommentRequestDto;
import aws.retrospective.dto.GetCommentsRequestDto;
import aws.retrospective.dto.GetCommentsResponseDto;
import aws.retrospective.dto.UpdateCommentRequestDto;
import aws.retrospective.dto.UpdateCommentResponseDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.User;
import aws.retrospective.repository.CommentRepository;
import aws.retrospective.repository.SectionCommentRepository;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
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
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private SectionCommentRepository sectionCommentRepository;
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

    @Test
    @DisplayName("모든 댓글 조회 테스트")
    public void testGetComments() {
        // Given
        GetCommentsRequestDto requestDto = new GetCommentsRequestDto();
        requestDto.setSectionId(1L);

        List<Comment> mockedComments = new ArrayList<>();
        // 가짜 댓글 목록 생성
        for (int i = 1; i <= 3; i++) {
            Comment comment = createComment();
            mockedComments.add(comment);
        }

        when(commentRepository.getCommentsWithSections(requestDto.getSectionId())).thenReturn(mockedComments);

        // When
        List<GetCommentsResponseDto> responseDtoList = commentService.getComments(requestDto);

        // Then
        assertEquals(mockedComments.size(), responseDtoList.size());
        for (int i = 0; i < mockedComments.size(); i++) {
            Comment mockedComment = mockedComments.get(i);
            GetCommentsResponseDto responseDto = responseDtoList.get(i);
            // 댓글 ID와 내용이 일치하는지 확인
            assertEquals(mockedComment.getId(), responseDto.getCommentId());
            assertEquals(mockedComment.getContent(), responseDto.getContent());

        }
    }

    private Comment createComment() {
        return Comment.builder().content("test").build();
    }

    private Section createSection() {
        return Section.builder().content("test").build();
    }




    private static Comment createComment(String content, User loginedUser) {
        return Comment.builder()
            .user(loginedUser)
            .content(content)
            .build();
    }


    private static User createUser() {
        return User.builder().username("test").phone("010-1234-1234").email("test@naver.com")
            .build();
    }

}