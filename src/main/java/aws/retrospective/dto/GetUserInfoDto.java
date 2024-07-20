package aws.retrospective.dto;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
}
