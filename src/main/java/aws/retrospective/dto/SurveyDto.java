package aws.retrospective.dto;

import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class SurveyDto {

    @NotNull
    private Integer age;

    @NotEmpty
    private String gender;

    @NotEmpty
    private String occupation;

    @NotEmpty
    private String region;

    @NotEmpty
    private String source;

    // 복수 선택이 가능한 purpose를 배열로 받음
    @NotEmpty
    private List<String> purpose;

    // 기타 입력값을 받음
    private String otherpurpose;

}