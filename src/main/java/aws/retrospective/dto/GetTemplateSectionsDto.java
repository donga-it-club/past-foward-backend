package aws.retrospective.dto;

import aws.retrospective.entity.SectionTemplateStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetTemplateSectionsDto {

    private Long id;
    private SectionTemplateStatus templateStatus;
    private Long templateId;
    private int sequence;

}
