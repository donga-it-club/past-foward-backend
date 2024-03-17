package aws.retrospective.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    BAD_REQUEST(400, "BAD_REQUEST_400"),
    NOT_FOUND(404, "NOT_FOUND_404"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR_500"),
    MISSING_REQUEST_PARAMETER(400, "MISSING REQUEST PARAMETER");

    private int code;
    private String message;
}