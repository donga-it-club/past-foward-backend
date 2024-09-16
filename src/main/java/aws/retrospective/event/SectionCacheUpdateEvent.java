package aws.retrospective.event;

import aws.retrospective.entity.Section;
import aws.retrospective.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SectionCacheUpdateEvent {

    private Long retrospectiveId;
    private Section section;
    private User user;

}
