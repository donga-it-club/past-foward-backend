package aws.retrospective.dto;

import aws.retrospective.entity.NoticeBoardWriting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeBoardWritingResponseDto {
    private String title;
    private String content;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private int views;

    // NoticeBoardWriting 엔티티를 받아서 초기화하는 생성자 추가
    public NoticeBoardWritingResponseDto(NoticeBoardWriting entity) {
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.status = entity.getStatus().name(); // Enum to String
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
        this.views = entity.getViews();
    }
}