package aws.retrospective.dto;

import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateSectionResponseDto {

    private Long userId;
    private Long retrospectiveId;
    private String sectionName;
    private String sectionContent;
    private int sequence; // 섹션에 등록된 게시물 순서
}
