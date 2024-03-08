package aws.retrospective.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateSectionResponseDto {

    private Long userId;
    private Long retrospectiveId;
    private String sectionName;
    private String sectionContent;
    private int sequence; // 섹션에 등록된 게시물 순서
}
