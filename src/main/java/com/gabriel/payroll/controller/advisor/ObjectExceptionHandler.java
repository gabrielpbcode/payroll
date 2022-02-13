package com.gabriel.payroll.controller.advisor;

import com.gabriel.payroll.dto.HttpResponse;
import com.gabriel.payroll.exception.DataAlreadyExistsException;
import com.gabriel.payroll.exception.DataNotFoundException;
import com.gabriel.payroll.exception.MissingDataException;
import com.gabriel.payroll.exception.PatternNotMatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class ObjectExceptionHandler implements ErrorController {

  private static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred while processing the request";
  private static final String ERROR_PATH = "/error";

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @ExceptionHandler(DataAlreadyExistsException.class)
  public ResponseEntity<HttpResponse> dataAlreadyExistException(DataAlreadyExistsException exception) {
    return createHttpResponse(BAD_REQUEST, exception.getMessage());
  }

  @ExceptionHandler(DataNotFoundException.class)
  public ResponseEntity<HttpResponse> dataNotFoundException(DataNotFoundException exception) {
    return createHttpResponse(BAD_REQUEST, exception.getMessage());
  }

  @ExceptionHandler(MissingDataException.class)
  public ResponseEntity<HttpResponse> missingDataException(MissingDataException exception) {
    return createHttpResponse(BAD_REQUEST, exception.getMessage());
  }

  @ExceptionHandler(PatternNotMatchException.class)
  public ResponseEntity<HttpResponse> patternNotMatchException(PatternNotMatchException exception) {
    return createHttpResponse(BAD_REQUEST, exception.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
    LOGGER.error(exception.getMessage());
    return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
  }

  private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus status, String message) {
    HttpResponse res = new HttpResponse(status.value(), status, status.getReasonPhrase().toUpperCase(), message);
    return new ResponseEntity<>(res, status);
  }

  @Override
  public String getErrorPath() {
    return ERROR_PATH;
  }

}
