package aws.retrospective.dto;

import aws.retrospective.entity.ProjectStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetRetrospectiveGroupResponseDto {

    private Long Id; //회고 그룹 id

    private String title; //회고 그룹 제목

    private Long userId; //회고 리더 id

    private String userName; //회고 리더 이름

    private String description; //회고 그룹 설명

    private UUID thumbnail; //회고 그룹 썸네일

    private String status; //회고 그룹 상태

}