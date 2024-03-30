package me.hikingcarrot7.privee.scheduling;

import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import me.hikingcarrot7.privee.events.InvitationExpiredEvent;
import me.hikingcarrot7.privee.models.invitation.Invitation;
import me.hikingcarrot7.privee.models.invitation.InvitationStatus;
import me.hikingcarrot7.privee.repositories.InvitationRepository;

import java.util.List;

@Singleton
public class CheckInvitationsValidityScheduler {
  @Inject private InvitationRepository invitationRepository;
  @Inject private Event<InvitationExpiredEvent> invitationEvent;

  @Schedule(second = "*/30", minute = "*", hour = "*", persistent = false)
  public void checkInvitationsValidity() {
    System.out.println("Checking pending invitations validity...");
    List<Invitation> pendingInvitations = invitationRepository.getPendingInvitations();
    pendingInvitations.forEach(invitation -> {
      if (invitation.isExpired()) {
        invitation.setStatus(InvitationStatus.EXPIRED);
        invitationRepository.save(invitation);
        invitationEvent.fire(new InvitationExpiredEvent(invitation.getResident()));
      }
    });
  }

}
