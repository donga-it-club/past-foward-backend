package aws.retrospective.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CreateSectionDto {

    private Long userId;
    private Long retrospectiveId;
    private Long templateSectionId;

    @NotEmpty(message = "Section content is required")
    private String sectionContent;
}