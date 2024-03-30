package aws.retrospective.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GetSectionsResponseDto {

    @Schema(description = "섹션 카드 id", example = "1")
    private Long sectionId;

    @Schema(description = "섹션 카드 등록자 이름", example = "hope")
    private String username;

    @Schema(description = "섹션 카드 내용", example = "문서 작성 - 수기를 담당하신 분이 작성한 회의록")
    private String content;

    @Schema(description = "섹션 카드 좋아요 수", example = "1")
    private long likeCnt;

    @Schema(description = "섹션 유형", example = "Kudos")
    private String sectionName;

    @Schema(description = "섹션 카드 등록일", example = "2021-07-01T00:00:00")
    private LocalDateTime createdDate;

    @QueryProjection
    public GetSectionsResponseDto(Long sectionId, String username, String content, long likeCnt,
        String sectionName, LocalDateTime createdDate) {
        this.sectionId = sectionId;
        this.username = username;
        this.content = content;
        this.likeCnt = likeCnt;
        this.sectionName = sectionName;
        this.createdDate = createdDate;
    }
}
