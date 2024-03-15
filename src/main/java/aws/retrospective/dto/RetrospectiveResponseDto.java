package aws.retrospective.dto;

import aws.retrospective.entity.Retrospective;
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

    public RetrospectiveResponseDto(Long id, String title, Long userId, Long teamId,
        Long templateId,
        String status,
        Boolean isBookmarked
    ) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.teamId = teamId;
        this.templateId = templateId;
        this.status = status;
        this.isBookmarked = isBookmarked;

    }

    public static RetrospectiveResponseDto of(Retrospective retrospective) {
        return new RetrospectiveResponseDto(
            retrospective.getId(),
            retrospective.getTitle(),
            retrospective.getUser().getId(),
            retrospective.getTeam() != null ? retrospective.getTeam().getId() : null,
            retrospective.getTemplate().getId(),
            retrospective.getStatus().name(),
            retrospective.getBookmarks().stream().anyMatch(
                bookmark -> bookmark.getUser().getId().equals(retrospective.getUser().getId()))
        );
    }
}
