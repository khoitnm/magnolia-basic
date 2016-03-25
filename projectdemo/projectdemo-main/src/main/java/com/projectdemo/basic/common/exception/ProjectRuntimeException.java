package com.projectdemo.basic.common.exception;

/**
 * This exception is used when something wrong happens without your expected.
 * This is similar to {@link RuntimeException}.
 * Error Code:<br/>
 * <li>1000 -> 1099: Error for input parameters.</li>
 * <li>1100 -> 1199: Error for repository (JCR, Database errors).</li>
 * <li>
 * 1200 -> 1499: Configuration error:
 * + 1200 : GeneralConfigurationException
 * + 1201 -> 1209: Category config
 * </li>
 * <li>1500: Internal Service Error.</li>
 * <li>1600: Content Browser Error.</li>
 * <li>1700: Email Error.</li>
 *
 * @author khoi.tran
 */

public class ProjectRuntimeException extends RuntimeException {
    public static final int DEFAULT_ERROR_CODE = 201;
    /**
     * Error code for not null/not blank fields.
     */
    public static final int ERROR_CODE_NOT_BLANK = 1001;

    /**
     * Error code for number field.
     */
    public static final int ERROR_CODE_NUMBER = 1003;
    public static final int ERROR_CODE_EMAIL = 1006;
    private final int _errorCode;

    public ProjectRuntimeException(String message) {
        this(DEFAULT_ERROR_CODE, message);
    }

    public ProjectRuntimeException(String message, Throwable cause) {
        this(DEFAULT_ERROR_CODE, message, cause);
    }

    public ProjectRuntimeException(int errorCode, String message) {
        super(message);
        _errorCode = errorCode;
    }

    public ProjectRuntimeException(int errorCode, Throwable cause) {
        super(cause);
        _errorCode = errorCode;
    }

    public ProjectRuntimeException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        _errorCode = errorCode;
    }

    public int getErrorCode() {
        return _errorCode;
    }
}
