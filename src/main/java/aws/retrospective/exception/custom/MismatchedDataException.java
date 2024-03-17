package aws.retrospective.exception.custom;

import aws.retrospective.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MismatchedDataException extends RuntimeException {

    ErrorCode errorCode;
}
