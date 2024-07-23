package aws.retrospective.dto;

import aws.retrospective.entity.SaveStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeBoardWritingRequestDto {
    private String title;
    private String content;
    private SaveStatus status;
    private UUID thumbnail;

}
