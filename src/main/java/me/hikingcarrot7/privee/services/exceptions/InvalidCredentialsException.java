package me.hikingcarrot7.privee.services.exceptions;

public class InvalidCredentialsException extends RuntimeException {

  public InvalidCredentialsException() {
    super("Invalid credentials");
  }

}
