package aws.retrospective.repository;

import aws.retrospective.dto.GetSectionsResponseDto;
import java.util.List;

public interface SectionRepositoryCustom {

    List<GetSectionsResponseDto> getSections2(Long retrospectiveId);

}