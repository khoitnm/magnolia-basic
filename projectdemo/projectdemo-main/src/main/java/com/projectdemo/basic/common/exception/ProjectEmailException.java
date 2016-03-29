package com.projectdemo.basic.common.exception;

/**
 * This exception is used for sending email.
 *
 * @author khoi.tran
 */

public class ProjectEmailException extends ProjectRuntimeException {
    public static final int ERROR_CODE = 1700;

    public ProjectEmailException(String message) {
        super(ERROR_CODE, message);
    }

    public ProjectEmailException(Throwable cause) {
        super(ERROR_CODE, cause);
    }

    public ProjectEmailException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }
}
