package aws.retrospective.repository;

import aws.retrospective.entity.Comment;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join c.section cs where cs.id = :sectionId")
    List<Comment> findCommentsBySectionId(@Param("sectionId") Long sectionId);

    Comment findTopBySectionIdOrderByCreatedDateDesc(Long sectionId);

    default Comment findLatestCommentBySection(Long sectionId) {
        return findTopBySectionIdOrderByCreatedDateDesc(sectionId);
    }

    List<Comment> findBySectionIdAndCreatedDateAfterOrderByCreatedDateDesc(Long sectionId,
        LocalDateTime createdDate);

    default List<Comment> findCommentsAfterDate(Long sectionId, LocalDateTime createdDate) {
        return findBySectionIdAndCreatedDateAfterOrderByCreatedDateDesc(sectionId, createdDate);
    }

    List<Comment> findAllBySectionIdOrderByCreatedDateDesc(Long sectionId);

    default List<Comment> findAllComments(Long sectionId) {
        return findAllBySectionIdOrderByCreatedDateDesc(sectionId);
    }
}