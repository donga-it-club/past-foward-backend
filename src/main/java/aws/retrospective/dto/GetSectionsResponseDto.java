package aws.retrospective.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetSectionsResponseDto {

    private Long sectionId;
    private String username;
    private String content;
    private long likeCnt;
    private String sectionName;
    private LocalDateTime createdDate;
}
