package aws.retrospective.dto;


import aws.retrospective.entity.ProjectStatus;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

public class GetRetrospectivesDto {

    int page = 0;

    @Min(1)
    int size = 10;

    RetrospectivesOrderType order = RetrospectivesOrderType.NEWEST;

    ProjectStatus status;

    String keyword;

    Boolean isBookmarked;

    //TODO: dto에서 받지 않도록 수정
    Long userId;


}
