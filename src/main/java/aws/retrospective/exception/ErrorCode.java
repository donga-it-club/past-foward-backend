package aws.retrospective.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    BAD_REQUEST(400, "BAD_REQUEST_400"),
    NOT_FOUND(404, "NOT_FOUND_404"),
    MISSING_REQUEST_PARAMETER(404, "MISSING REQUEST PARAMETER"),
    EMPTY_DATA_ACCESS(404, "EMPTY DATA ACCESS"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR_500");

    private int code;
    private String message;
}