package aws.retrospective.repository;

// 캐싱과 관련한 기능을 제공하는 인터페이스
public interface CacheRepository<T> {

    String getCacheKey();

    T getCacheDate(String cacheKey);

    void saveCacheData(String cacheKey, T data);

}
