package me.hikingcarrot7.privee.services.exceptions;

public class InvitationAlreadyVerifiedException extends RuntimeException {

  public InvitationAlreadyVerifiedException(String token) {
    super(String.format("Invitation with token %s has already been verified", token));
  }

}
