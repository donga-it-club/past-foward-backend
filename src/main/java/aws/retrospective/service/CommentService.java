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
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;


    // 모든 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentDto> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // 특정 댓글 조회
    @Transactional(readOnly = true)
    public CommentDto getCommentDTOById(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        return convertToDTO(comment);
    }

    // 댓글 생성
    @Transactional
    public Comment createComment(CreateCommentDto createCommentDto) {
        // 댓글의 작성자 정보 가져오기
        User user = userRepository.findById(createCommentDto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + createCommentDto.getUserId()));

        // 댓글이 속한 섹션 정보 가져오기
        Section section = sectionRepository.findById(createCommentDto.getCommentSectionId())
            .orElseThrow(() -> new IllegalArgumentException("Section not found with ID: " + createCommentDto.getCommentSectionId()));

        // 새 댓글 생성
        Comment comment = Comment.builder()
            .content(createCommentDto.getCommentContent())
            .user(user)
            .section(section)
            .createDate(createCommentDto.getCreateDate())
            .build();

        return commentRepository.save(comment);
    }


    // 댓글 수정
    @Transactional
    public Comment updateComment(UpdateCommentDto updateCommentDto) {
        // 수정할 댓글 정보 가져오기
        Comment commentToUpdate = commentRepository.findById(updateCommentDto.getCommentId())
            .orElseThrow(() -> new IllegalArgumentException("Comment not found with ID: " + updateCommentDto.getCommentId()));

        // 댓글을 수정한 사용자 정보 가져오기
        User user = userRepository.findById(updateCommentDto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + updateCommentDto.getUserId()));

        // 수정을 시도한 섹션 정보 가져오기
        Section section = sectionRepository.findById(updateCommentDto.getCommentSectionId())
            .orElseThrow(() -> new IllegalArgumentException("Section not found with ID: " + updateCommentDto.getCommentSectionId()));

        // 댓글을 작성한 사용자와 수정을 시도한 사용자가 동일한지 확인
        if (!commentToUpdate.getUser().getId().equals(updateCommentDto.getUserId())) {
            throw new IllegalArgumentException("User does not have permission to update this comment.");
        }

        // 댓글 내용 수정
        commentToUpdate.updateContent(updateCommentDto.getUpdatedContent());

        return commentRepository.save(commentToUpdate);
    }




    // 댓글 삭제
    @Transactional
    public void deleteComment(Long id) {
        Comment commentToDelete = findCommentById(id);
        commentRepository.delete(commentToDelete);
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
    }


    private CommentDto convertToDTO(Comment comment) {
        return new CommentDto(comment.getId(), comment.getContent());
        // 필요한 다른 필드들도 엔티티에서 DTO로 복사합니다.
    }
}