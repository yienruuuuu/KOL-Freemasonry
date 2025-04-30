package io.github.yienruuuuu.exception;

import java.io.Serializable;

public interface ErrorCode extends Serializable {

    String getMessage();

    Integer getCode();

    String name();

}