package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetCommentsResponseDto {

    @Schema(description = "comment id", example = "1")
    private Long commentId;

    @Schema(description = "user name", example = "llcodingll")
    private String username;

    @Schema(description = "댓글 내용", example = "커피 마시고 싶어요... 제발.")
    private String content;

    @Schema(description = "댓글 등록일", example = "2021-07-01T00:00:00")
    private LocalDateTime createdDate;


}