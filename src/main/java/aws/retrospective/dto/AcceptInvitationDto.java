package aws.retrospective.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class AcceptInvitationDto {


    @NotEmpty
    private String invitationCode;
}
