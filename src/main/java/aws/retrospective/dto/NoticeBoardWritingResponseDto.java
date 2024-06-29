package aws.retrospective.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeBoardWritingResponseDto {
    private String title;
    private String content;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}