package aws.retrospective.exception.custom;

import aws.retrospective.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ForbiddenAccessException extends RuntimeException {

    private ErrorCode errorCode;

    public ForbiddenAccessException(String errorMessage) {
        super(errorMessage);
        this.errorCode = ErrorCode.FORBIDDEN_ACCESS;
    }
}