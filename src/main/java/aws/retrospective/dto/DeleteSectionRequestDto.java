package aws.retrospective.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeleteSectionRequestDto {

    @NotNull(message = "사용자 id는 필수 정보입니다.")
    private Long userId;
}
