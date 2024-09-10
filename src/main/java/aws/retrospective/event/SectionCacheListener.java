package aws.retrospective.event;

import aws.retrospective.dto.GetSectionsResponseDto;
import aws.retrospective.repository.CacheRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Slf4j
public class SectionCacheListener {

    private final CacheRepository<List<GetSectionsResponseDto>> cacheRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateCacheWithNewSection(SectionCacheUpdateEvent event) {
        String cacheKey = String.format("%s::%d", cacheRepository.getCacheKey(),
            event.getRetrospectiveId());

        try {
            // 캐싱된 데이터를 가져온다.
            List<GetSectionsResponseDto> cachingData = cacheRepository.getCacheDate(cacheKey);

            // 캐싱된 값이 존재할 경우 생성된 회고 카드를 추가한다.
            if (cachingData != null && !cachingData.isEmpty()) {
                cachingData.add(GetSectionsResponseDto.from(event.getSection(), event.getUser()));
                cacheRepository.saveCacheData(cacheKey, cachingData);
            }
        } catch (Exception ex) { // TODO 커스텀 예외 생성한다.
            // 캐싱되어 있는 데이터를 삭제한다.
            cacheRepository.deleteCacheData(cacheKey);
            log.error("캐싱 데이터 갱신 중 오류가 발생했습니다.", ex);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void deleteCacheHandle(SectionCacheDeleteEvent event) {
        String cacheKey = String.format("%s::%d", cacheRepository.getCacheKey(),
            event.getRetrospectiveId());
        cacheRepository.deleteCacheData(cacheKey);
    }

}
