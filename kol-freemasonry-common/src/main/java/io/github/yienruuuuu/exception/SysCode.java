package io.github.yienruuuuu.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Eric.Lee
 */

@Getter
@AllArgsConstructor
public enum SysCode implements ErrorCode {
    OK(1000, "成功"),
    FAIL(1001, "Expected error"),

    NOT_FOUND(7000, "Data not found"),
    ERROR(9999, "Unexpected error");

    private final Integer code;
    private final String message;
}