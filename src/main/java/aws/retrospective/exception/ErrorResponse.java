package aws.retrospective.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private int status;
    private String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
