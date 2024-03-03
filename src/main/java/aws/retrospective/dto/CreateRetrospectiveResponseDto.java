package aws.retrospective.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateRetrospectiveResponseDto {
    private Long id;
    private String title;
    private Long teamId;
    private Long userId;
    private Long templateId;
}
