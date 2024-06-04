package aws.retrospective.dto;

import aws.retrospective.entity.RetrospectiveGroup;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RetrospectiveGroupResponseDto {

    private Long id;
    private String title;
    private Long userId;
    private String username;
    private String status;
    private Boolean isBookmarked;
    private UUID thumbnail;
    private String description;
    private LocalDateTime updatedDate;

    public RetrospectiveGroupResponseDto(Long id, String title, Long userId, String username,
        String status,
        Boolean isBookmarked,
        UUID thumbnail,
        String description,
        LocalDateTime updatedDate
    ) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.username = username;
        this.status = status;
        this.isBookmarked = isBookmarked;
        this.thumbnail = thumbnail;
        this.description = description;
        this.updatedDate = updatedDate;

    }

    public static RetrospectiveGroupResponseDto of(RetrospectiveGroup retrospectiveGroup,
        boolean hasBookmarksByUser) {
        return new RetrospectiveGroupResponseDto(
            retrospectiveGroup.getId(),
            retrospectiveGroup.getTitle(),
            retrospectiveGroup.getUser().getId(),
            retrospectiveGroup.getUser().getUsername(),
            retrospectiveGroup.getStatus().name(),
            hasBookmarksByUser,
            retrospectiveGroup.getThumbnail(),
            retrospectiveGroup.getDescription(),
            retrospectiveGroup.getUpdatedDate()
        );
    }
}
