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
import java.util.Objects;

@ApplicationScoped
public class GatekeeperService {
  @Inject private GatekeeperRepository gatekeeperRepository;
  @Inject private PasswordEncoder passwordEncoder;

  public List<Gatekeeper> getAllGatekeepers(PageRequest pageRequest) {
    return gatekeeperRepository.findAll(pageRequest);
  }

  public Gatekeeper getGatekeeperById(String id) {
    Gatekeeper gatekeeper = gatekeeperRepository.findById(id);
    if (Objects.isNull(gatekeeper)) {
      throw new ResourceNotFoundException(String.format("Gatekeeper with id %s not found", id));
    }
    return gatekeeper;
  }

  public Gatekeeper getGatekeeperByEmail(String email) {
    Gatekeeper gatekeeper = gatekeeperRepository.findByEmail(email);
    if (Objects.isNull(gatekeeper)) {
      throw new ResourceNotFoundException(String.format("Gatekeeper with email %s not found", email));
    }
    return gatekeeper;
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
