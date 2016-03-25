package com.projectdemo.basic.common.exception;

/**
 * This exception is an unchecked Exception, which is different from {@link javax.jcr.RepositoryException} (a checked Exception).
 * This exception is used when you have any error while accessing data from Repository (e.g. JCR).
 *
 * @author khoi.tran
 */

public class ProjectRepositoryException extends ProjectRuntimeException {
    public static final int ERROR_CODE = 1100;

    public ProjectRepositoryException(String message) {
        super(ERROR_CODE, message);
    }

    public ProjectRepositoryException(Throwable cause) {
        super(ERROR_CODE, cause);
    }

    public ProjectRepositoryException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }
}
