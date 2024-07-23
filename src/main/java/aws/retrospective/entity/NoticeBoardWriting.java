package aws.retrospective.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeBoardWriting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noticeboardwriting_id")
    private Long id;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING) // JPA가 enum 타입을 데이터베이스에 매핑하는 방식을 지정 -> 문자열로 저장할 것임
    private SaveStatus status; // 'PUBLISHED', 'TEMP'
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private int views;
    private UUID thumbnail;

    @Builder
    public NoticeBoardWriting(String title,String content, SaveStatus status, UUID thumbnail) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.thumbnail = thumbnail;
    }

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }

    public void updateBoard(String title, String content, UUID thumbnail) {
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
    }
}
