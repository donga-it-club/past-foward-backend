package aws.retrospective.service;


import aws.retrospective.dto.CreateCommentDto;
import aws.retrospective.dto.CreateCommentResponseDto;
import aws.retrospective.dto.GetCommentsRequestDto;
import aws.retrospective.dto.GetCommentsResponseDto;
import aws.retrospective.dto.UpdateCommentRequestDto;
import aws.retrospective.dto.UpdateCommentResponseDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.User;
import aws.retrospective.exception.custom.ForbiddenAccessException;
import aws.retrospective.repository.CommentRepository;
import aws.retrospective.repository.SectionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final SectionRepository sectionRepository;


    // 댓글 생성
    @Transactional
    public CreateCommentResponseDto createComment(User user, CreateCommentDto request) {

        // 댓글이 속한 섹션 정보 가져오기
        Section sectionId = sectionRepository.findById(request.getSectionId())
            .orElseThrow(() -> new NoSuchElementException("Section not found with ID"));

        // 새 댓글 생성
        Comment createComment = createComment(request.getCommentContent(), sectionId, user);
        commentRepository.save(createComment);

        return new CreateCommentResponseDto(createComment.getUser().getId(), user.getId(),
            request.getSectionId(), request.getCommentContent());
    }

    private Comment createComment(String commentContent, Section findSection, User user) {
        return Comment.builder()
            .content(commentContent)
            .section(findSection)
            .user(user)
            .build();
    }


    // 댓글 수정
    @Transactional
    public UpdateCommentResponseDto updateCommentContent(User user, Long commentId, UpdateCommentRequestDto request) {
        Comment findComment = getComment(commentId);

        // 댓글 내용 수정은 작성자만 가능하다.
        if (!findComment.getUser().getId().equals(user.getId())) {
            throw new ForbiddenAccessException("댓글을 수정할 권한이 없습니다.");
        }

        // 댓글 수정
        findComment.updateComment(request.getCommentContent());

        return new UpdateCommentResponseDto(commentId, request.getCommentContent());
    }


    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment findComment = commentRepository.findById(commentId)
            .orElseThrow(() -> new NoSuchElementException("Comment not found"));

        // 작성자만 댓글을 삭제할 수 있다.
        if (!findComment.getUser().getId().equals(user.getId())) {
            throw new ForbiddenAccessException("작성자만 댓글을 삭제할 수 있습니다.");
        }

        commentRepository.delete(findComment);

    }

    // 모든 댓글 조회
    @Transactional(readOnly = true)
    public List<GetCommentsResponseDto> getComments(GetCommentsRequestDto request) {

        List<GetCommentsResponseDto> response = new ArrayList<>();
        List<Comment> comments = commentRepository.getCommentsWithSections(request.getSectionId());

        revertDto(comments, response);

        return response;
    }

    private void revertDto(List<Comment> comments, List<GetCommentsResponseDto> response) {
        for (Comment comment : comments) {
            response.add(
                new GetCommentsResponseDto(
                    comment.getId(),
                    comment.getUser().getUsername(),
                    comment.getContent(),
                    comment.getCreatedDate()
                ));
        }
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new NoSuchElementException("Comment not found with ID: " + commentId));
    }

}