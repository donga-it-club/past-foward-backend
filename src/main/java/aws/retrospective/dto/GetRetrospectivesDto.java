package aws.retrospective.dto;


import aws.retrospective.entity.ProjectStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

public class GetRetrospectivesDto {

    int page = 0;

    int size = 10;

    RetrospectivesOrderType order = RetrospectivesOrderType.RECENTLY;

    ProjectStatus status;

    String keyword;

    Boolean isBookmarked;


}
