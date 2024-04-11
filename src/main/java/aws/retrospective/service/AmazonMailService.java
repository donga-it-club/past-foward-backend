package aws.retrospective.service;

import aws.retrospective.dto.EmailSenderDto;
import aws.retrospective.dto.SendMailRequestDto;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AmazonMailService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    @Transactional
    public void sendMail(SendMailRequestDto request) {
        EmailSenderDto dto = new EmailSenderDto();
        amazonSimpleEmailService.sendEmail(dto.toSendRequestDto(request));
    }
}
