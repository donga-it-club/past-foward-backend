package aws.retrospective.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;

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

    @Builder
    public NoticeBoardWriting(String title,String content, SaveStatus status) {
        this.title = title;
        this.content = content;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }

    // 상태 업데이트 메서드
    public void updateStatus(SaveStatus status) {
        this.status = status;
    }

    public void incrementViews() {
        this.views++;
    }
}