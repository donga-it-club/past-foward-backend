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

import lombok.*;


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

    // 이메일 수신 동의 상태 업데이트
    private boolean isemailConsent; // 이메일 수신동의 여부

    @Builder
    public User(String email, String username, String phone, String tenantId, boolean isAdministrator, boolean isEmailConsent) {
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.tenantId = tenantId;
        this.isAdministrator = isAdministrator;
        this.isemailConsent = isEmailConsent;
    }

    // 프로필 이미지 등록
    public void updateUserInfo(String thumbnail, String username) {
        this.thumbnail = thumbnail;
        this.username = username;
    }

    // 이메일 수신 동의 상태 업데이트
    public void updateEmailConsent(boolean isemailConsent) {
        this.isemailConsent = isemailConsent;
    }

    public void updateAdministrator(boolean isAdministrator) {
        this.isAdministrator = isAdministrator;
    }
}