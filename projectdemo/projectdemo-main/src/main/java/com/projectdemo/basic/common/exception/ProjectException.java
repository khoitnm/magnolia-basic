package com.projectdemo.basic.common.exception;

/**
 * @author khoi.tran
 */

public class ProjectException extends Exception {
    public ProjectException() {
        super();
    }

    public ProjectException(String message) {
        super(message);
    }

    public ProjectException(Throwable cause) {
        super(cause);
    }

    public ProjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
