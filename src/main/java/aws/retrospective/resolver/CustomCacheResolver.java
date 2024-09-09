package aws.retrospective.resolver;

import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomCacheResolver implements CacheResolver {

    private final CacheManager cacheManager;

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        String cacheName = context.getOperation().getCacheNames().iterator().next();
        return Collections.singleton(cacheManager.getCache(cacheName));
    }
}
