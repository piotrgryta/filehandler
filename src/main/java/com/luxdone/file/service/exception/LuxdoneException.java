package com.luxdone.file.service.exception;

import lombok.Getter;

@Getter
public class LuxdoneException extends RuntimeException {
  private final int httpStatus;

  public LuxdoneException(LuxdoneExceptionMessage exceptionMessage) {
    super(exceptionMessage.getMessage());
    this.httpStatus = exceptionMessage.getHttpStatus();
  }

}
