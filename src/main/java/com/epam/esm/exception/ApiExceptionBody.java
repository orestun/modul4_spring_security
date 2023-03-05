package com.epam.esm.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author orest uzhytchak
 * Class that include all need fields for representation of custom errors
 * */
public class ApiExceptionBody {
    @JsonView
    private final String errorMassage;
    @JsonView
    private final Long errorCode;
    @JsonView
    private HttpStatus httpStatus;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime timestamp;

    public ApiExceptionBody(String errorMassage, Long errorCode, HttpStatus httpStatus) {
        this.errorMassage = errorMassage;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.timestamp = LocalDateTime.now(ZoneId.of("UTC+02:00"));
    }

}
