package aws.retrospective.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateRetrospectiveDto {
    @NotEmpty(message = "Retrospective title is required.")
    String title;

    Long teamId;

    //TODO: 추후 Cognito 이용해서 request body에서 받지 않고 perssitence layer에서 받아오도록 수정
    Long userId;

    @NotNull(message = "Template id is required.")
    Long templateId;
}
