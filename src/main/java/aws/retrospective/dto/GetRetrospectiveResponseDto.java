package aws.retrospective.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetRetrospectiveResponseDto {

    private Long retrospectiveId; // 회고 ID
    private String title; // 회고 제목
    private Long templateId; // 회고 템플릿 유형 (Kudos, KPT..)
    private RetrospectiveType type; // 회고 유형 (팀, 개인)
    private Long userId; // 회고 리더(생성자)
    private String leaderName; // 회고 리더 이름
    private String description; // 회고 설명
    private String status; // 회고 상태 (진행중, 완료)
    private UUID thumbnail; // 회고 썸네일
}
