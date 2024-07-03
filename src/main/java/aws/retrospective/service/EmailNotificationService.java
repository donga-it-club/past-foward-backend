package aws.retrospective.service;

import aws.retrospective.entity.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

// 이메일 전송내용
@Component
@RequiredArgsConstructor
public class EmailNotificationService {

    private JavaMailSender mailSender;

    private static final String EMAIL_TITLE_PREFIX = "[PastFoward] ";

    @Async
    public void sendNotificationEmail(Notification notification) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // 메일 제목 설정
        helper.setSubject(EMAIL_TITLE_PREFIX + "Please check your retrospective");
        // 메일 수신자 설정
        helper.setTo(notification.getReceiver().getEmail());

        // 메일 내용 설정
        String text = "Hello, n\nYou have received a notification:\n\n +" +
                notification.getSender().getUsername() +
                notification.getNotificationType() +
                "to you";
        helper.setText(text, true);

        // 메일 전송
        mailSender.send(message);
    }
}
