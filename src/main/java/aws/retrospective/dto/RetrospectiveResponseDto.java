package aws.retrospective.dto;

import aws.retrospective.entity.Retrospective;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RetrospectiveResponseDto {

    private Long id;
    private String title;
    private Long userId;
    private Long teamId;
    private Long templateId;
    private String status;
    private Boolean isBookmarked;
    private UUID thumbnail;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public RetrospectiveResponseDto(Long id, String title, Long userId, Long teamId,
        Long templateId,
        String status,
        Boolean isBookmarked,
        UUID thumbnail,
        LocalDateTime createdDate,
        LocalDateTime updatedDate
    ) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.teamId = teamId;
        this.templateId = templateId;
        this.status = status;
        this.isBookmarked = isBookmarked;
        this.thumbnail = thumbnail;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;

    }

    public static RetrospectiveResponseDto of(Retrospective retrospective,
        boolean hasBookmarksByUser) {
        return new RetrospectiveResponseDto(
            retrospective.getId(),
            retrospective.getTitle(),
            retrospective.getUser().getId(),
            retrospective.getTeam() != null ? retrospective.getTeam().getId() : null,
            retrospective.getTemplate().getId(),
            retrospective.getStatus().name(),
            hasBookmarksByUser,
            retrospective.getThumbnail(),
            retrospective.getCreatedDate(),
            retrospective.getUpdatedDate()
        );
    }
}
