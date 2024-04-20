package aws.retrospective.exception;

import aws.retrospective.exception.custom.ForbiddenAccessException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validMissingParameterException(
        MethodArgumentNotValidException ex) {
        log.error("유효성 검사 실패", ex);
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        ErrorResponse response = new ErrorResponse(ErrorCode.MISSING_REQUEST_PARAMETER,
            errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException occurred", ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        log.error("ResponseStatusException occurred", ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST, ex.getReason());
        if (ex.getStatusCode() == HttpStatus.CONFLICT) {
            response = new ErrorResponse(ErrorCode.CONFILCT, ex.getReason());
        }
        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
        IllegalArgumentException ex) {
        log.error("잘못된 인자 전달", ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
        log.error("엔티티 조회 실패", ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.EMPTY_DATA_ACCESS, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenAccessException(
        ForbiddenAccessException ex) {
        log.error("ForbiddenAccessException occurred", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> receiveRuntimeException(RuntimeException ex) {
        log.error("RuntimeException occurred", ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Exception occurred", ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
