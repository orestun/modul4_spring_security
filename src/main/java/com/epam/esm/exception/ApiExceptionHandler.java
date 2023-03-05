package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author orest uzhytchak
 * Class that handle custom errors
 * */
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Handle method that handle {@link ItemNotFoundException}
     * exceptions that came from service layer
     * */
    @ExceptionHandler(value = {ItemNotFoundException.class})
    public ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException e){
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiExceptionBody exceptionBody =
                new ApiExceptionBody(
                        e.getMessage(),
                        e.getErrorCode(),
                        httpStatus);
        return new ResponseEntity<>(exceptionBody, httpStatus);
    }

    /**
     * Handle method that handle {@link ObjectAlreadyExistsException}
     * exceptions that came from service layer
     * */
    @ExceptionHandler(value = {ObjectAlreadyExistsException.class})
    public ResponseEntity<Object> handleObjectAlreadyExistsException(ObjectAlreadyExistsException e){
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ApiExceptionBody exceptionBody =
                new ApiExceptionBody(
                        e.getMessage(),
                        e.getErrorCode(),
                        httpStatus);
        return new ResponseEntity<>(exceptionBody, httpStatus);
    }

    /**
     * Handle method that handle {@link HibernateValidationException}
     * exceptions that came from service layer
     * */
    @ExceptionHandler(value = {HibernateValidationException.class})
    public ResponseEntity<Object> handleHibernateValidationException(HibernateValidationException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiExceptionBody exceptionBody =
                new ApiExceptionBody(
                        e.getMessage(),
                        e.getErrorCode(),
                        httpStatus);
        return new ResponseEntity<>(exceptionBody, httpStatus);
    }

    /**
     * Handle method that handle {@link NotAllowedParameterException}
     * exceptions that came from service layer
     * */
    @ExceptionHandler(value = {NotAllowedParameterException.class})
    public ResponseEntity<Object> handleNotAllowedParameterException(NotAllowedParameterException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiExceptionBody exceptionBody =
                new ApiExceptionBody(
                        e.getMessage(),
                        e.getErrorCode(),
                        httpStatus);
        return new ResponseEntity<>(exceptionBody, httpStatus);
    }
}
