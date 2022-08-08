package com.example.oidc.exception.room;

public class CustomEmptyRoomIsNotExistException extends RuntimeException {

  public CustomEmptyRoomIsNotExistException(String msg, Throwable t) {
    super(msg, t);
  }

  public CustomEmptyRoomIsNotExistException(String msg) {
    super(msg);
  }

  public CustomEmptyRoomIsNotExistException() {
    super();
  }
}
