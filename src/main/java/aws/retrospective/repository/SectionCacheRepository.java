package aws.retrospective.repository;

import org.springframework.stereotype.Repository;

@Repository
public class SectionCacheRepository implements CacheRepository {

    public static final String CACHE_KEY = "sectionsInRetrospective-cache";

    private final String cacheKey = CACHE_KEY;

    @Override
    public String getCacheKey() {
        return cacheKey;
    }
}
