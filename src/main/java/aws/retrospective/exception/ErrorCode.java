package aws.retrospective.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    BAD_REQUEST(400, "BAD_REQUEST_400"),
    FORBIDDEN_ACCESS(403, "리소스 접근 권한이 없습니다."),
    NOT_FOUND(404, "NOT_FOUND_404"),
    MISSING_REQUEST_PARAMETER(404, "MISSING REQUEST PARAMETER"),
    EMPTY_DATA_ACCESS(404, "EMPTY DATA ACCESS"),
    CONFILCT(409, "CONFLICT_409"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR_500");

    private int code;
    private String message;
}