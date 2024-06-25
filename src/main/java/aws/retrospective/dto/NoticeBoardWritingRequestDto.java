package aws.retrospective.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeBoardWritingRequestDto {
    private String title;
    private String content;
}