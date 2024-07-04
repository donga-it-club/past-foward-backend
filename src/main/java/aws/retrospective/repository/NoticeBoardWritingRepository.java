package aws.retrospective.repository;

import aws.retrospective.entity.NoticeBoardWriting;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface NoticeBoardWritingRepository extends JpaRepository<NoticeBoardWriting, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<NoticeBoardWriting> findById(Long id);
}