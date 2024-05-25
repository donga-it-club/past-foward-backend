package aws.retrospective.repository;

import aws.retrospective.entity.redis.NotificationRedis;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRedisRepository extends CrudRepository<NotificationRedis, String> {

}
