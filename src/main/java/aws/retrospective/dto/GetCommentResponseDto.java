package aws.retrospective.dto;

import aws.retrospective.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GetCommentResponseDto {

    @Schema(description = "회고 ID", example = "1")
    private Long retrospectiveId;
    @Schema(description = "회고 카드 ID", example = "1")
    private Long sectionId;
    @Schema(description = "댓글 작성자", example = "이희망")
    private String username;
    @Schema(description = "회고 이름", example = "중간 회고")
    private String retrospectiveName;
    @Schema(description = "댓글 작성 시간", example = "2024-05-10T00:00:00")
    private LocalDateTime createdDate;

    private GetCommentResponseDto(Long retrospectiveId, Long sectionId, String username,
        String retrospectiveName, LocalDateTime createdDate) {
        this.retrospectiveId = retrospectiveId;
        this.sectionId = sectionId;
        this.username = username;
        this.retrospectiveName = retrospectiveName;
        this.createdDate = createdDate;
    }

    public static GetCommentResponseDto createResponse(Comment comment) {
        return new GetCommentResponseDto(comment.getSection().getRetrospective().getId(),
            comment.getSection()
                .getId(), comment.getUser().getUsername(),
            comment.getSection().getRetrospective().getTitle(), comment.getCreatedDate());
    }
}
