package aws.retrospective.dto;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class EmailSenderDto {


    public SendEmailRequest toSendRequestDto(SendMailRequestDto request, String to) {
        System.out.println("to: " + to);
        Destination destination = new Destination()
            .withToAddresses(to);

        Message message = new Message()
            .withSubject(createContent(
                String.format("[PF - %s] - %s", request.getMailStatus().getName(),
                    request.getSubject())))
            .withBody(new Body()
                .withHtml(createContent(createHtmlBody(request))));

        return new SendEmailRequest()
            .withSource(request.getFrom())
            .withDestination(destination)
            .withMessage(message);
    }

    private static Content createContent(final String text) {
        return new Content()
            .withCharset("UTF-8")
            .withData(text);
    }

    private static String createHtmlBody(SendMailRequestDto request) {
        return "<div style=\" font-family: Arial, sans-serif;\">" +
            "<div style=\"margin-bottom: 20px;\">" +
            "<strong>작성자 이메일:</strong> " + request.getFrom() + "</div>" +
            "<div style=\"margin-bottom: 20px;\">" +
            "<strong>문의 유형:</strong> " + request.getMailStatus().getName() + "</div>" +
            "<div style=\"margin-bottom: 20px;\">" +
            "<strong>문의 제목:</strong> " + request.getSubject() + "</div>" +
            "<div style=\"margin-bottom: 20px;\">" +
            "<strong>문의 내용:</strong> " + request.getContent() + "</div>" +
            "</div>";
    }
}