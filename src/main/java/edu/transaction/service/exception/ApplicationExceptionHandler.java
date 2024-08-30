package edu.transaction.service.exception;

import edu.transaction.service.dto.ApplicationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<Object> exceptionHandler(TransactionException ex) {
        ApplicationException exception = new ApplicationException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        log.error("Transaction Exception | exception: {}, status:{}", ex.getMessage(), exception.getStatus());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApplicationException> exceptionHandler(RuntimeException ex) {
        ApplicationException exception = new ApplicationException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        log.error("Runtime exception | exception: {}, status: {}", ex.getMessage(), exception.getStatus());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApplicationException> exceptionHandler(ValidationException ex) {
        ApplicationException exception = new ApplicationException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        log.error("Validation exception | exception: {}, status: {}", ex.getMessage(), exception.getStatus());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }
}
