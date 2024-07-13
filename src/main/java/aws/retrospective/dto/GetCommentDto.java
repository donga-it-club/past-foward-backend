package aws.retrospective.dto;

import aws.retrospective.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class GetCommentDto {

    @Schema(description = "회고카드 id", example = "1")
    Long sectionId;
    @Schema(description = "댓글 id", example = "1")
    private Long commentId;
    @Schema(description = "작성자 id", example = "1")
    private Long userId;
    @Schema(description = "댓글 내용", example = "매우 공감됩니다!")
    private String content;
    @Schema(description = "댓글 작성자", example = "hope")
    private String username;
    @Schema(description = "프로필 이미지", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String thumbnail;
    @Schema(description = "댓글 등록일", example = "2021-07-01T00:00:00")
    private LocalDateTime createdDate;
    @Schema(description = "댓글 수정일", example = "2021-07-01T00:00:00")
    private LocalDateTime lastModifiedDate;

    public static GetCommentDto from(Comment comment) {
        return new GetCommentDto(comment.getSection().getId(), comment.getId(), comment.getUser().getId(), comment.getContent(),
            comment.getUser().getUsername(), comment.getUser().getThumbnail(), comment.getCreatedDate(), comment.getUpdatedDate());
    }
}
