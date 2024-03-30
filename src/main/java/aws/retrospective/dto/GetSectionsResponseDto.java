package aws.retrospective.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GetSectionsResponseDto {

    @Schema(description = "section id", example = "1")
    private Long sectionId;

    @Schema(description = "user name", example = "hope")
    private String username;

    @Schema(description = "작성 내용", example = "노션의 페이지 작성을 잘했다.")
    private String content;

    @Schema(description = "좋아요 개수", example = "3")
    private long likeCnt;

    @Schema(description = "섹션 유형", example = "Keep")
    private String sectionName;

    @Schema(description = "작성일", example = "2021-07-01T00:00:00")
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
