package com.example.oidc.exception.room;

public class CustomRoomOvercapacityException extends RuntimeException {

  public CustomRoomOvercapacityException(String msg, Throwable t) {
    super(msg, t);
  }

  public CustomRoomOvercapacityException(String msg) {
    super(msg);
  }

  public CustomRoomOvercapacityException() {
    super();
  }
}
