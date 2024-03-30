package me.hikingcarrot7.privee.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import me.hikingcarrot7.privee.events.InvitationCreatedEvent;
import me.hikingcarrot7.privee.models.CloudFile;
import me.hikingcarrot7.privee.models.invitation.Invitation;
import me.hikingcarrot7.privee.models.invitation.InvitationStatus;
import me.hikingcarrot7.privee.models.invitation.InvitationVerification;
import me.hikingcarrot7.privee.repositories.InvitationRepository;
import me.hikingcarrot7.privee.repositories.InvitationVerificationRepository;
import me.hikingcarrot7.privee.services.exceptions.InvitationAlreadyVerifiedException;
import me.hikingcarrot7.privee.services.exceptions.InvitationExpiredException;
import me.hikingcarrot7.privee.services.exceptions.ResourceNotFoundException;
import me.hikingcarrot7.privee.web.dtos.InvitationsIntervalParams;
import me.hikingcarrot7.privee.web.dtos.pagination.PageRequest;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class InvitationService {
  @Inject private InvitationRepository invitationRepository;
  @Inject private InvitationVerificationRepository invitationVerificationRepository;
  @Inject private ResidentService residentService;
  @Inject private GatekeeperService gatekeeperService;
  @Inject private QRCodeImageGeneratorService qrCodeImageGeneratorService;
  @Inject private ImageUploaderService imageUploaderService;
  @Inject private Event<InvitationCreatedEvent> invitationCreatedEvent;

  public List<Invitation> getInvitationsOfResident(String residentId, PageRequest pageRequest) {
    return invitationRepository.getInvitationsOfResident(residentId, pageRequest);
  }

  public List<Invitation> getInvitationsOnInterval(String residentId, InvitationsIntervalParams intervalParams) {
    return invitationRepository.getInvitationsOnInterval(residentId, intervalParams);
  }

  public Invitation getInvitationById(String id) {
    return invitationRepository.findById(id);
  }

  public List<Invitation> getPendingInvitations() {
    return invitationRepository.getPendingInvitations();
  }

  public Invitation createInvitation(String residentId, Invitation invitation) {
    return tryCreateInvitation(residentId, invitation);
  }

  private Invitation tryCreateInvitation(String residentId, Invitation invitation) {
    try {
      String invitationToken = UUID.randomUUID().toString();
      BufferedImage qrCode = qrCodeImageGeneratorService.tryToGenerateQRCodeImage(invitationToken);
      CloudFile cloudFile = imageUploaderService.uploadImage(qrCode, invitationToken);

      invitation.setStatus(InvitationStatus.PENDING);
      invitation.setToken(invitationToken);
      invitation.setQrCode(cloudFile);

      residentService.addInvitationToResident(residentId, invitation);
      invitationCreatedEvent.fire(new InvitationCreatedEvent(residentService.getResidentById(residentId)));
      return invitationRepository.findByToken(invitationToken);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void validateInvitation(String token, String gatekeeperId) {
    Invitation invitation = invitationRepository.findByToken(token);
    if (Objects.isNull(invitation)) {
      throw new ResourceNotFoundException(String.format("Invitation with token %s not found", token));
    }
    throwIfInvitationAlreadyVerifiedOrExpired(invitation);

    InvitationVerification verification = new InvitationVerification();
    verification.setInvitation(invitation);
    invitation.setVerification(verification);
    invitation.setStatus(InvitationStatus.VERIFIED);

    gatekeeperService.addVerificationToGatekeeper(gatekeeperId, verification);
    invitationVerificationRepository.save(verification);
    invitationRepository.save(invitation);
  }

  public void cancelInvitation(String invitationId) {
    Invitation invitation = getInvitationById(invitationId);
    if (Objects.isNull(invitation)) {
      throw new ResourceNotFoundException(String.format("Invitation with id %s not found", invitationId));
    }
    throwIfInvitationAlreadyVerifiedOrExpired(invitation);

    invitation.setStatus(InvitationStatus.CANCELED);
    invitationRepository.save(invitation);
  }

  private void throwIfInvitationAlreadyVerifiedOrExpired(Invitation invitation) {
    if (invitation.isAlreadyVerified()) {
      throw new InvitationAlreadyVerifiedException(invitation.getToken());
    }
    if (invitation.isExpired()) {
      throw new InvitationExpiredException(invitation.getToken());
    }
  }

}
