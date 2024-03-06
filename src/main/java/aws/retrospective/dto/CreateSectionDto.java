package aws.retrospective.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateSectionDto {

    private Long userId;
    private Long retrospectiveId;
    @NotEmpty(message = "Section location is required")
    private String sectionName;
    @NotEmpty(message = "Section content is required")
    private String sectionContent;
}