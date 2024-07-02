package aws.retrospective.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeBoardWritingRequestDto {
    private String title;
    private String content;
}
