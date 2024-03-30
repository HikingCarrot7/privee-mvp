package me.hikingcarrot7.privee.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.hikingcarrot7.privee.models.Gatekeeper;
import me.hikingcarrot7.privee.models.invitation.InvitationVerification;
import me.hikingcarrot7.privee.repositories.GatekeeperRepository;
import me.hikingcarrot7.privee.services.exceptions.ResourceNotFoundException;
import me.hikingcarrot7.privee.utils.PasswordEncoder;
import me.hikingcarrot7.privee.web.dtos.pagination.PageRequest;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GatekeeperService {
  @Inject private GatekeeperRepository gatekeeperRepository;
  @Inject private PasswordEncoder passwordEncoder;

  public List<Gatekeeper> getAllGatekeepers(PageRequest pageRequest) {
    return gatekeeperRepository.findAll(pageRequest);
  }

  public Gatekeeper getGatekeeperById(String gatekeeperId) {
    Optional<Gatekeeper> gatekeeperMaybe = gatekeeperRepository.findById(gatekeeperId);
    return gatekeeperMaybe.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Gatekeeper with gatekeeperId %s not found", gatekeeperId))
    );
  }

  public Gatekeeper getGatekeeperByEmail(String email) {
    Optional<Gatekeeper> gatekeeperMaybe = gatekeeperRepository.findByEmail(email);
    return gatekeeperMaybe.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Gatekeeper with email %s not found", email))
    );
  }

  public Gatekeeper saveGatekeeper(Gatekeeper gatekeeper) {
    gatekeeper.setPassword(passwordEncoder.encode(gatekeeper.getPassword()));
    return gatekeeperRepository.save(gatekeeper);
  }

  public Gatekeeper updateGatekeeper(String id, Gatekeeper gatekeeper) {
    Gatekeeper gatekeeperToUpdate = getGatekeeperById(id);

    gatekeeperToUpdate.setFirstName(gatekeeper.getFirstName());
    gatekeeperToUpdate.setLastName(gatekeeper.getLastName());
    gatekeeperToUpdate.setPhone(gatekeeper.getPhone());

    return gatekeeperRepository.save(gatekeeperToUpdate);
  }

  public void addVerificationToGatekeeper(String gatekeeperId, InvitationVerification verification) {
    Gatekeeper gatekeeper = getGatekeeperById(gatekeeperId);
    gatekeeper.addVerification(verification);
    gatekeeperRepository.save(gatekeeper);
  }

  public void deleteGatekeeper(String id) {
    gatekeeperRepository.delete(id);
  }

}
