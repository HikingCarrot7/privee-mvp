package me.hikingcarrot7.privee.events;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class InvitationExpiredEventListener {

  public void onInvitationExpiredEvent(@Observes InvitationExpiredEvent invitationExpiredEvent) {
    String residentEmail = invitationExpiredEvent.getResident().getEmail();
    System.out.printf("Sending email to resident %s to notify invitation expiration%n", residentEmail);
  }

}
