package edu.transaction.service.exception;

import edu.transaction.service.dto.ApplicationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<Object> exceptionHandler(TransactionException ex) {
        ApplicationException exception = new ApplicationException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        log.error("Transaction Exception | ошибка: {}, статус:{}", ex.getMessage(), exception.getStatus());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApplicationException> exceptionHandler(RuntimeException ex) {
        ApplicationException exception = new ApplicationException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        log.error("Generic exception | ошибка: {}, статус: {}", ex.getMessage(), exception.getStatus());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApplicationException> exceptionHandler(ValidationException ex) {
        ApplicationException exception = new ApplicationException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        log.error("Validation exception | ошибка: {}, статус: {}", ex.getMessage(), exception.getStatus());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
