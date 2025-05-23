package io.github.yienruuuuu.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private final ErrorCode code;
    private final Object data;
    private HttpStatus httpStatus = HttpStatus.EXPECTATION_FAILED;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode;
        this.data = null;
    }

    public ApiException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode;
        this.data = null;
    }

    public ApiException(ErrorCode errorCode, Object data) {
        super(errorCode.getMessage());
        this.code = errorCode;
        this.data = data;
    }

    public ApiException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode.getMessage());
        this.code = errorCode;
        this.data = null;
        this.httpStatus = httpStatus;
    }

    public ApiException(ErrorCode errorCode, HttpStatus httpStatus, Object data) {
        super(errorCode.getMessage());
        this.code = errorCode;
        this.data = data;
        this.httpStatus = httpStatus;
    }
}
