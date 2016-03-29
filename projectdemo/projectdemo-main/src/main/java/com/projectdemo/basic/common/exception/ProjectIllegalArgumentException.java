package com.projectdemo.basic.common.exception;

/**
 * This exception is used when arguments of a method is invalid (null, not string, not number...).
 *
 * @author khoi.tran
 */

public class ProjectIllegalArgumentException extends ProjectRuntimeException {
    public static final int ERROR_CODE = 201;

    public ProjectIllegalArgumentException(String message) {
        super(ERROR_CODE, message);
    }

    public ProjectIllegalArgumentException(Throwable cause) {
        super(ERROR_CODE, cause);
    }

    public ProjectIllegalArgumentException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }
}
