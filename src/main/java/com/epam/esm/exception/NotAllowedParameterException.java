package com.epam.esm.exception;

/**
 * @author orest uzhytchak
 * Custom exceptions for errors if some parametres are not alloved
 * */
public class NotAllowedParameterException extends RuntimeException{
    Long errorCode;


    public NotAllowedParameterException(String message, Long errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public Long getErrorCode() {
        return errorCode;
    }
}
