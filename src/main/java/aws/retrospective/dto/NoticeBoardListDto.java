package aws.retrospective.dto;

import aws.retrospective.entity.NoticeBoardWriting;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeBoardListDto {
    private Long id;
    private String title;
    private LocalDateTime createdDate;
    private int views;

    public NoticeBoardListDto(NoticeBoardWriting entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.createdDate = entity.getCreatedDate();
        this.views = entity.getViews();
    }
}