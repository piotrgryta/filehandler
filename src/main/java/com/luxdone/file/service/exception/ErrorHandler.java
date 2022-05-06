package com.luxdone.file.service.exception;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({LuxdoneException.class})
  public ResponseEntity<Object> handleLuxdoneException(@NotNull LuxdoneException appException,
                                                       WebRequest request) {
    if (logger.isTraceEnabled()) {
      logger.trace("LuxdoneException", appException);
    }
    logger.info(appException.toString());
    var responseMessage = new GenericErrorMessage(appException.getHttpStatus(), appException.getMessage());
    return new ResponseEntity<>(responseMessage, HttpStatus.resolve(appException.getHttpStatus()));
  }


  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers, HttpStatus status,
                                                                WebRequest request) {
    return new ResponseEntity<>(new GenericErrorMessage(HttpStatus.BAD_REQUEST.value(),
        ex.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList())), HttpStatus.BAD_REQUEST);

  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                HttpHeaders headers, HttpStatus status,
                                                                WebRequest request) {
    return new ResponseEntity<>(new GenericErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage()),
        HttpStatus.BAD_REQUEST);

  }

  @ExceptionHandler({Throwable.class})
  public ResponseEntity<Object> handleEverythingElse(Throwable e, WebRequest request) {
    logger.error("Error", e);
    GenericErrorMessage responseMessage =
        new GenericErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ToString
  @Getter
  @AllArgsConstructor
  private static class GenericErrorMessage {
    int httpStatus;
    List<String> message;

    public GenericErrorMessage(int httpStatus, String message) {
      this.httpStatus = httpStatus;
      this.message = List.of(message);
    }
  }
}
