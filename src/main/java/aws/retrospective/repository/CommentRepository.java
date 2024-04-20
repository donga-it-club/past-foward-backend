package aws.retrospective.repository;

import aws.retrospective.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom{

    @Query("select c from Comment c join c.section cs where sc.id = :sectionId")
    List<Comment> getCommentsWithSections(@Param("sectionId") Long sectionId);
}