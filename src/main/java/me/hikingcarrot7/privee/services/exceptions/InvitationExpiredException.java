package me.hikingcarrot7.privee.services.exceptions;

public class InvitationExpiredException extends RuntimeException {

  public InvitationExpiredException(String token) {
    super(String.format("Invitation with token %s has expired", token));
  }

}
