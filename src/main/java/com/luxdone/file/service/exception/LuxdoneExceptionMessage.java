package com.luxdone.file.service.exception;

import java.net.HttpURLConnection;
import lombok.Getter;

@Getter
public enum LuxdoneExceptionMessage {
  WRONG_FILE_BYTES_MESSAGE("File has no bytes or is malformed", HttpURLConnection.HTTP_BAD_REQUEST),
  NO_FILE_FOR_GIVEN_ID_MESSAGE("No file for given id", HttpURLConnection.HTTP_NOT_FOUND),
  UPDATE_NOT_POSSIBLE_MESSAGE("Update not possible, file has different extension than previously uploaded", HttpURLConnection.HTTP_FORBIDDEN);


  private final String message;
  private final int httpStatus;

  LuxdoneExceptionMessage(String message, int httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
