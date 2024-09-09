package aws.retrospective.event;

import aws.retrospective.dto.GetSectionsResponseDto;
import aws.retrospective.repository.CacheRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class SectionCacheListener {

    private final CacheRepository<List<GetSectionsResponseDto>> cacheRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateCacheWithNewSection(SectionCacheUpdateEvent event) {
        String cacheKey = String.format("%s::%d", cacheRepository.getCacheKey(),
            event.getRetrospectiveId());

        // 캐싱된 데이터를 가져온다.
        List<GetSectionsResponseDto> cachingData = cacheRepository.getCacheDate(cacheKey);

        // 캐싱된 값이 존재할 경우 생성된 회고 카드를 추가한다.
        if (cachingData != null && !cachingData.isEmpty()) {
            cachingData.add(GetSectionsResponseDto.from(event.getSection(), event.getUser()));
            cacheRepository.saveCacheData(cacheKey, cachingData);
        }
    }
}
