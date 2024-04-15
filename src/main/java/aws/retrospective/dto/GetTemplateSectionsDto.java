package aws.retrospective.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetTemplateSectionsDto {

    private Long id;
    private String name;
    private Long templateId;
    private int sequence;

}
