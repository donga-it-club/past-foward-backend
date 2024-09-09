package aws.retrospective.repository;

import aws.retrospective.dto.GetSectionsResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SectionCacheRepository implements CacheRepository {

    public static final String CACHE_KEY = "sectionsInRetrospective-cache";
    private final String cacheKey = CACHE_KEY;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public String getCacheKey() {
        return cacheKey;
    }

    @Override
    public List<GetSectionsResponseDto> getCacheDate(String cacheKey) {
        return (List<GetSectionsResponseDto>) redisTemplate.opsForValue().get(cacheKey);
    }

    @Override
    public void saveCacheData(String cacheKey, Object data) {
        redisTemplate.opsForValue().set(cacheKey, data);
    }

    @Override
    public void deleteCacheData(String cacheKey) {
        redisTemplate.delete(cacheKey);
    }
}
