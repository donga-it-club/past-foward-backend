package aws.retrospective.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RetrospectiveTemplateResponseDto {

    private Long id;
    private String name;
}
