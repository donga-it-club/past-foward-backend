package aws.retrospective.exception.retrospective;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TemplateMisMatchException extends RuntimeException{

    private final RetrospectiveErrorCode errorCode; // TODO ErrorCode로 변경

}
