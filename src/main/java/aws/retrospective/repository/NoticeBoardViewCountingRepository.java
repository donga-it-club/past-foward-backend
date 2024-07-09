package aws.retrospective.repository;

import aws.retrospective.entity.NoticeBoardViewCounting;
import org.springframework.data.repository.CrudRepository;

public interface NoticeBoardViewCountingRepository extends CrudRepository<NoticeBoardViewCounting, Long> {

}
