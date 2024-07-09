package aws.retrospective.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PagedResponseDto<T> {
    private List<T> posts;
    private int totalPages;
}
