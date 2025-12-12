package com.twaszak.payments.exceptions;

import com.google.common.base.Throwables;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class PurchaseTransactionExceptionHandler {

    /**
     * This method handles the error when a date in improper format is passed in the request. This with return a meaningful message to the user of the api
     * @param httpMessageNotReadableException
     * @return message for the bad request
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException) {
        Objects.requireNonNull(httpMessageNotReadableException);
        Throwable rootCause = Throwables.getRootCause(httpMessageNotReadableException);
        if(rootCause instanceof DateTimeParseException)
        {
            return  new ResponseEntity<>(Map.of("message","Date must be in UTC and follow the format: yyyy-MM-dd'T'HH:mm:ss"), HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity<>(Map.of("message","Bad Request"), HttpStatus.BAD_REQUEST);
    }

    /**
     * This method handles error messages for invalid parameters
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleEventValidationException(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> errors = methodArgumentNotValidException.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, PurchaseTransactionExceptionHandler::getErrorMessage));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    private static String getErrorMessage(FieldError fieldError) {
        return fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "internal service error";
    }
}
