package aws.retrospective.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email; // 사용자 이메일

    private String username; // 사용자 이름

    private String phone; // 전화번호

    @JsonIgnore
    private String tenantId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserTeam> teams = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Likes> likes = new ArrayList<>();

    private String thumbnail; // 프로필 이미지
    private boolean isAdministrator; // 관리자 여부

    @Builder
    public User(String email, String username, String phone, String tenantId, boolean isAdministrator) {
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.tenantId = tenantId;
        this.isAdministrator = isAdministrator;
    }

    // 프로필 이미지 등록
    public void updateUserInfo(String thumbnail, String username) {
        this.thumbnail = thumbnail;
        this.username = username;
    }

    // 관리자 여부 업데이트
    public void updateAdministrator(boolean isAdministrator) {
        this.isAdministrator = isAdministrator;
    }
}