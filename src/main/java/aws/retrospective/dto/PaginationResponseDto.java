package aws.retrospective.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public record PaginationResponseDto<T>(long totalCount, List<T> nodes) {

    public static <T, U> PaginationResponseDto<U> fromPage(Page<T> page,
        Function<T, U> converter) {
        List<U> dtoList = page.getContent().stream()
            .map(converter)
            .collect(Collectors.toList());
        return new PaginationResponseDto<>(page.getTotalElements(), dtoList);
    }
}

