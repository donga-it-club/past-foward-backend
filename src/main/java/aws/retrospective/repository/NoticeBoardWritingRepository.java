package aws.retrospective.repository;

import aws.retrospective.entity.NoticeBoardWriting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeBoardWritingRepository extends JpaRepository<NoticeBoardWriting, Long> {
}