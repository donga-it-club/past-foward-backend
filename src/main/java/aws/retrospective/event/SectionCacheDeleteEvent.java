package aws.retrospective.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SectionCacheDeleteEvent {

    private Long retrospectiveId;

}
