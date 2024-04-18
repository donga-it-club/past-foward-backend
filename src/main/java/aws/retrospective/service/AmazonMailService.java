package aws.retrospective.service;

import aws.retrospective.dto.EmailSenderDto;
import aws.retrospective.dto.SendMailRequestDto;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmazonMailService {

    @Value("${aws.ses.send-mail-to}")
    private String to;


    private final AmazonSimpleEmailService amazonSimpleEmailService;

    public void sendMail(SendMailRequestDto request) {
        EmailSenderDto dto = new EmailSenderDto();
        SendEmailResult sendResult = amazonSimpleEmailService.sendEmail(
            dto.toSendRequestDto(request, to));

        // 메일이 정상적으로 전송되면 200 OK를 반환한다.
        if (sendResult.getSdkHttpMetadata().getHttpStatusCode() == 200) {
            log.info("Email sent successfully, Email: {}", request.getFrom());
        } else {
            log.error("Failed to send email, Email: {}", request.getFrom());
        }

    }
}
