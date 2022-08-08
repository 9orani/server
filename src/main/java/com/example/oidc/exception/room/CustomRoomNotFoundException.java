package com.example.oidc.exception.room;

public class CustomRoomNotFoundException extends RuntimeException {

  public CustomRoomNotFoundException(String msg, Throwable t) {
    super(msg, t);
  }

  public CustomRoomNotFoundException(String msg) {
    super(msg);
  }

  public CustomRoomNotFoundException() {
    super();
  }
}
