package aws.retrospective.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminRoleDTO {

    private String email;
    private boolean isAdmin;
}