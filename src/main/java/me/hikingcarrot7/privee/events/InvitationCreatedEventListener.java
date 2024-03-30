package me.hikingcarrot7.privee.events;

import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Observes;

@Singleton
public class InvitationCreatedEventListener {

  public void onInvitationCreated(@Observes InvitationCreatedEvent event) {
    String residentEmail = event.getResident().getEmail();
    System.out.println("Sending email to resident:" + residentEmail);
  }

}
