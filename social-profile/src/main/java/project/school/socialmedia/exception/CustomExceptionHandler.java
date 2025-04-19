package project.school.socialmedia.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = NoSuchElementException.class)
  public ResponseEntity<Object> entityNotFound(Exception exception, WebRequest request) {
    ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), HttpStatus.NOT_FOUND);
    return buildResponseEntity(errorDetails);
  }

  @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
  public ResponseEntity<Object> sqlConstraint(Exception exception, WebRequest request) {
    ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), HttpStatus.BAD_REQUEST);
    return buildResponseEntity(errorDetails);
  }

  @ExceptionHandler(value = IllegalArgumentException.class)
  public ResponseEntity<Object> illegalArgumentConstraint(Exception exception, WebRequest request) {
    ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), HttpStatus.BAD_REQUEST);
    return buildResponseEntity(errorDetails);
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<Object> globalException(Exception exception, WebRequest request) {
    ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    return buildResponseEntity(errorDetails);
  }

  private ResponseEntity<Object> buildResponseEntity(ErrorDetails errorDetails) {
    return new ResponseEntity<>(errorDetails, errorDetails.getStatus());
  }
}
