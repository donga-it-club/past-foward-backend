package aws.retrospective.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class GetSectionsResponseDto {

    @Schema(description = "회고 카드 id", example = "1")
    private Long sectionId;

    @Schema(description = "사용자 이름", example = "hope")
    private String username;

    @Schema(description = "회고 카드 작성 내용", example = "노션의 페이지 작성을 잘했다.")
    private String content;

    @Schema(description = "회고 카드 좋아요 개수", example = "3")
    private long likeCnt;

    @Schema(description = "섹션 유형", example = "Keep")
    private String sectionName;

    @Schema(description = "회고 카드 등록일", example = "2021-07-01T00:00:00")
    private LocalDateTime createdDate;

    @Schema(description = "프로필 이미지", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String thumbnail;

    private List<GetCommentDto> comments = new ArrayList<>();

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

    public GetSectionsResponseDto(Long sectionId, String username, String content, long likeCnt,
        String sectionName, LocalDateTime createdDate, List<GetCommentDto> comments, String thumbnail) {
        this.sectionId = sectionId;
        this.username = username;
        this.content = content;
        this.likeCnt = likeCnt;
        this.sectionName = sectionName;
        this.createdDate = createdDate;
        this.comments = comments;
        this.thumbnail = thumbnail;
    }
}
