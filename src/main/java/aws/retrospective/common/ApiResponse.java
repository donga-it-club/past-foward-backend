package aws.retrospective.common;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse<T> {

    private int code; // 처리 코드
    private String message; // 예외 시에 전송 할 예외 메시지
    private T data; // 정상처리 시에 전송 할 데이터

    @Builder
    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static<T> ApiResponse<T> successResponse(HttpStatus status, T data) {
        return ApiResponse.<T>builder()
            .code(status.value())
            .message(null)
            .data(data)
            .build();
    }

    public static<T>ApiResponse<T> errorResponse(HttpStatus status, String message) {
        return ApiResponse.<T>builder()
            .code(status.value())
            .message(message)
            .data(null)
            .build();
    }
}
