package aws.retrospective.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeBoardWritingResponseDto {
    private Long id;
    private String title;
    private String content;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public NoticeBoardWritingResponseDto(Long id, String title, String content, String status, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
