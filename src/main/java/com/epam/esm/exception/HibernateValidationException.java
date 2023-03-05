package com.epam.esm.exception;

/**
 * @author orest uzhytchak
 * Custom exceptions for errors occured by validation
 * */
public class HibernateValidationException extends RuntimeException{
    Long errorCode;

    public HibernateValidationException(String message, Long errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public Long getErrorCode() {
        return errorCode;
    }
}
