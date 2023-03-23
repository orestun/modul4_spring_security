package com.epam.esm.exception;

public class BadAuthenticationData extends RuntimeException{
    Long errorCode;

    public BadAuthenticationData(String message, Long errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public Long getErrorCode() {
        return errorCode;
    }
}
