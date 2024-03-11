package aws.retrospective.service;

import aws.retrospective.entity.Comment;
import aws.retrospective.repository.CommentRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentId(Long id) {
        return commentRepository.findById(id);
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }
    public Comment updateComment(Long id, Comment updatedComment) {
        Optional<Comment> existingComment = commentRepository.findById(id);

        if (existingComment.isPresent()) {
            Comment commentToUpdate = existingComment.get();
            // Update relevant fields of the existing comment with new values from updatedComment
            // e.g., commentToUpdate.setText(updatedComment.getText());
            return commentRepository.save(commentToUpdate);
        } else {
            // Handle the case where the comment with the given ID is not found
            // You may throw an exception or handle it according to your application's requirements
            return null;
        }
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }



}
