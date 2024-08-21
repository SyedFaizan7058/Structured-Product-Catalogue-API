package com.nit.handler;

import com.nit.exception.ExceptionInfo;
import com.nit.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsHandler {

//    Explanation:
//    ex.getBindingResult().getFieldErrors(): Gets the list of field errors.
//    .map(...): Maps each field error to a simple string with the field name, class name, and error message.
//    .findFirst().orElse("Validation error"): Returns the first error message or a default message if none are found.

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionInfo> handleValidationExceptions(MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> "Field '" + fieldError.getField() + "' in class '"
                        + fieldError.getObjectName() + "' - " + fieldError.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        ExceptionInfo exceptionInfo = new ExceptionInfo();
        exceptionInfo.setTimestamp(LocalDateTime.now());
        exceptionInfo.setStatus(String.valueOf(ex.getStatusCode()));
        exceptionInfo.setMessage(errorMessage);

        return new ResponseEntity<>(exceptionInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionInfo> handleUnauthorizedException(Exception ex) {

        String errorMessage = ex.getMessage();
        ExceptionInfo exceptionInfo = new ExceptionInfo();
        exceptionInfo.setTimestamp(LocalDateTime.now());
        exceptionInfo.setMessage(errorMessage);
        exceptionInfo.setStatus(ex.getClass().getSimpleName());

        return new ResponseEntity<>(exceptionInfo, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionInfo> handleInternalException(Exception ex) {

        ExceptionInfo exceptionInfo = new ExceptionInfo();
        exceptionInfo.setTimestamp(LocalDateTime.now());
        exceptionInfo.setMessage("Server issue, please try after some time !");
        exceptionInfo.setStatus(ex.getClass().getSimpleName());

        return new ResponseEntity<>(exceptionInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
