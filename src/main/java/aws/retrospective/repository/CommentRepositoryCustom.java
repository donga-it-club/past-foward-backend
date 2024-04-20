package aws.retrospective.repository;

import aws.retrospective.dto.GetCommentsResponseDto;
import java.util.List;

public interface CommentRepositoryCustom {

    List<GetCommentsResponseDto> getComments(Long sectionId);
}