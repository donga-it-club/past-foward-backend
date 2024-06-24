package aws.retrospective.service;

import aws.retrospective.dto.CreateCommentDto;
import aws.retrospective.dto.CreateCommentResponseDto;
import aws.retrospective.dto.GetCommentsResponseDto;
import aws.retrospective.dto.UpdateCommentRequestDto;
import aws.retrospective.dto.UpdateCommentResponseDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.entity.Notification;
import aws.retrospective.entity.NotificationType;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.User;
import aws.retrospective.exception.custom.ForbiddenAccessException;
import aws.retrospective.repository.CommentRepository;
import aws.retrospective.repository.NotificationRepository;
import aws.retrospective.repository.SectionRepository;
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
    private final NotificationRepository notificationRepository;


    // 댓글 생성
    @Transactional
    public CreateCommentResponseDto createComment(User user, CreateCommentDto request) {

        // 댓글이 속한 섹션 정보 가져오기
        Section section = sectionRepository.findById(request.getSectionId())
            .orElseThrow(() -> new NoSuchElementException("Section not found with ID"));

        // 새 댓글 생성
        Comment createComment = createComment(request.getCommentContent(), section, user);
        commentRepository.save(createComment);

        Notification notification = createNotification(user, section, createComment);
        notificationRepository.save(notification);

        return new CreateCommentResponseDto(
            createComment.getId(),
            createComment.getUser().getId(),
            createComment.getSection().getId(),
            createComment.getContent()
        );
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
    public UpdateCommentResponseDto updateCommentContent(User user, Long commentId,
        UpdateCommentRequestDto request) {
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

        notificationRepository.findNotificationByCommentId(commentId)
            .ifPresent(notificationRepository::delete);

        commentRepository.delete(findComment);
    }

    // 모든 댓글 조회
    @Transactional(readOnly = true)
    public List<GetCommentsResponseDto> getComments(User user, Long sectionId) {

        Section findSection = sectionRepository.findById(sectionId)
            .orElseThrow(() -> new NoSuchElementException("Section not found with ID"));

        List<Comment> comments = commentRepository.findCommentsBySectionId(sectionId);

        return comments.stream()
            .map(comment -> new GetCommentsResponseDto(comment.getId(),
                comment.getUser().getUsername(),
                comment.getContent(), comment.getCreatedDate()))
            .toList();
    }


    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(
                () -> new NoSuchElementException("Comment not found with ID: " + commentId));
    }

    private static Notification createNotification(User user, Section section, Comment comment) {
        return Notification.builder().notificationType(NotificationType.COMMENT)
            .retrospective(section.getRetrospective()).section(section).sender(comment.getUser())
            .receiver(section.getUser()).comment(comment)
            .build();
    }

}