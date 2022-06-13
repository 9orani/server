package com.example.oidc.exception.player;

public class CustomPlayerNotFoundException extends RuntimeException {

  public CustomPlayerNotFoundException(String msg, Throwable t) {
    super(msg, t);
  }

  public CustomPlayerNotFoundException(String msg) {
    super(msg);
  }

  public CustomPlayerNotFoundException() {
    super();
  }
}
