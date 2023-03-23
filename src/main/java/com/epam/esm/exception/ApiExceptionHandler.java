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
                getApiExceptionBody(e, httpStatus, e.getErrorCode());
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
                getApiExceptionBody(e, httpStatus, e.getErrorCode());
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
                getApiExceptionBody(e, httpStatus, e.getErrorCode());
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
                getApiExceptionBody(e, httpStatus, e.getErrorCode());
        return new ResponseEntity<>(exceptionBody, httpStatus);
    }

    @ExceptionHandler(value = {BadAuthenticationData.class})
    public ResponseEntity<Object> handleBadAuthenticationDataException(BadAuthenticationData e){
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ApiExceptionBody exceptionBody =
                getApiExceptionBody(e, httpStatus, e.getErrorCode());
        return new ResponseEntity<>(exceptionBody, httpStatus);
    }

    private ApiExceptionBody getApiExceptionBody(Exception e, HttpStatus status, Long errorCode){
        return new ApiExceptionBody(
                e.getMessage(),
                errorCode,
                status);
    }
}
