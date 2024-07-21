package aws.retrospective.dto;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class GetUserInfoDto {

    private Long userId;
    private String userName;
    private String email;
    private String thumbnail;
    private String phone;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private boolean isAdministrator; // 관리자 여부 추가

    // Getter methods
    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }
}
