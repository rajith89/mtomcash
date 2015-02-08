package com.ontag.mcash.admin.web.exception;

public class ServiceException extends Exception {

    public static final int PROCESSING_FAILED = 1;
    public static final int VALIDATION_FAILED = 2;
    public static final int VALUE_DUPLICATION = 3;

    private int code;

    public ServiceException() {
        super();
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
