package aws.retrospective.dto;

import aws.retrospective.entity.Comment;
import aws.retrospective.entity.Likes;
import aws.retrospective.entity.Section;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SectionNotificationDto {

    @Schema(description = "조회한 회고 카드 ID", example = "1")
    private Long sectionId; // 조회한 Section ID
    @Schema(description = "회고 카드가 속한 회고 보드 ID", example = "2")
    private Long retrospectiveId;
    @Schema(description = "회고 카드가 속한 회고 보드 이름", example = "중간 회고")
    private String retrospectiveName; // Retrospective 이름

    @Schema(description = "알림을 보낼 댓글 List")
    private List<GetCommentResponseDto> comments;
    @Schema(description = "알림을 보낼 좋아요 List")
    private List<GetLikeResponseDto> likes;

    public static SectionNotificationDto createNotification(Section section, List<Comment> comments,
        List<Likes> likes) {
        return new SectionNotificationDto(
            section.getId(),
            section.getRetrospective().getId(),
            section.getRetrospective().getTitle(),
            comments.stream().map(GetCommentResponseDto::of).toList(),
            likes.stream().map(GetLikeResponseDto::of).toList()
        );
    }
}
