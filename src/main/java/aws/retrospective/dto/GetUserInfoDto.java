package aws.retrospective.dto;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserInfoDto {

    private Long userId;
    private String userName;
    private String email;
    private String thumbnail;
    private String phone;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
