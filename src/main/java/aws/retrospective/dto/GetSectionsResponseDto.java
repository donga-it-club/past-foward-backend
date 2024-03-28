package aws.retrospective.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GetSectionsResponseDto {

    private Long sectionId;
    private String username;
    private String content;
    private long likeCnt;
    private String sectionName;
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
