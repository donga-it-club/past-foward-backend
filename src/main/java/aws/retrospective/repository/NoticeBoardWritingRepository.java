package aws.retrospective.repository;

import aws.retrospective.entity.NoticeBoardWriting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeBoardWritingRepository extends JpaRepository<NoticeBoardWriting, Long> {
    Optional<NoticeBoardWriting> findById(Long id);
}