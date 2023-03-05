package com.epam.esm.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

/**
 * @author orest uzhytchak
 * Class that representates errors occured in validation
 * */
public class DataValidationHandler <T>{

    /**
     * Method that handle errors got during validation
     *
     * @param elementForValidation a generic type object that was validated by
     *                             hibernate validator
     * @return string of one or more errors
     * */
    public String errorsRepresentation(T elementForValidation){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(elementForValidation);
        StringBuilder errors = new StringBuilder();
        for (ConstraintViolation<T> violation : violations) {
            errors.append(violation.getMessage()).append("; ");
        }
        return errors.toString();
    }
}
