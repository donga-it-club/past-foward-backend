package aws.retrospective.exception.retrospective;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum RetrospectiveErrorCode {

    TEMPLATE_MISMATCH(HttpStatus.BAD_REQUEST, "회고보드 템플릿과 일치하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
