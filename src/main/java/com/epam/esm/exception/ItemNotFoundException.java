package com.epam.esm.exception;

/**
 * @author orest uzhytchak
 * Custom exceptions for errors occured if some item is non-existent
 * */
public class ItemNotFoundException extends RuntimeException {
    private final Long errorCode;
    public ItemNotFoundException(String message, Long errorCode){
        super(message);
        this.errorCode = errorCode;
    }


    public Long getErrorCode() {
        return errorCode;
    }
}
