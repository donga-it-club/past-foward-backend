package aws.retrospective.dto;

import aws.retrospective.entity.MailStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SendMailRequestDto {

    @NotNull(message = "보내는 사람 이메일 주소는 필수 정보 입니다.")
    @Schema(description = "보내는 사람 이메일 주소", example = "example@gmail.com")
    private String from;

    @NotNull(message = "메일 제목은 필수 정보 입니다.")
    @Schema(description = "메일 제목", example = "제목")
    private String subject;

    @NotNull(message = "메일 내용은 필수 정보 입니다.")
    @Schema(description = "메일 내용", example = "내용")
    private String content;

    @NotNull(message = "문의 유형은 필수 정보 입니다.")
    @Schema(description = "문의 유형", example = "SITE")
    private MailStatus mailStatus;
}
