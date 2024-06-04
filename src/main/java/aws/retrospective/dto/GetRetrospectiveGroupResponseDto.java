package aws.retrospective.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetRetrospectiveGroupResponseDto {

    private Long retrospectiveGroupId; // 회고 그룹 ID
    private String title; // 회고 그룹 제목
    private Long userId; // 회고 그룹 생성자
    private String description; // 회고 그룹 설명
    private String status; // 회고 상태 (진행중, 완료)
    private UUID thumbnail; // 회고 그룹 썸네일

}
