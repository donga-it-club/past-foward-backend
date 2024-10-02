package aws.retrospective.repository;

import aws.retrospective.dto.GetSectionsResponseDto;
import aws.retrospective.entity.Section;
import java.util.List;

public interface SectionRepositoryCustom {

    List<GetSectionsResponseDto> getSectionsAll(Long retrospectiveId);

    List<GetSectionsResponseDto> getSections2(Long retrospectiveId);

}