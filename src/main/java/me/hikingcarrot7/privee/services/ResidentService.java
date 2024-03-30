package me.hikingcarrot7.privee.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.hikingcarrot7.privee.models.Resident;
import me.hikingcarrot7.privee.models.invitation.Invitation;
import me.hikingcarrot7.privee.repositories.ResidentRepository;
import me.hikingcarrot7.privee.services.exceptions.ResourceNotFoundException;
import me.hikingcarrot7.privee.utils.PasswordEncoder;
import me.hikingcarrot7.privee.web.dtos.pagination.PageRequest;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ResidentService {
  @Inject private ResidentRepository residentRepository;
  @Inject private PasswordEncoder passwordEncoder;

  public List<Resident> getResidents(PageRequest pageRequest) {
    return residentRepository.findAll(pageRequest);
  }

  public Resident getResidentById(String residentId) {
    Optional<Resident> residentMaybe = residentRepository.findById(residentId);
    return residentMaybe.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Resident with residentId %s not found", residentId))
    );
  }

  public Resident getResidentByEmail(String email) {
    Optional<Resident> residentMaybe = residentRepository.findByEmail(email);
    return residentMaybe.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Resident with email %s not found", email))
    );
  }

  public Resident saveResident(Resident resident) {
    resident.setPassword(passwordEncoder.encode(resident.getPassword()));
    return residentRepository.save(resident);
  }

  public Resident updateResident(String residentId, Resident resident) {
    Resident residentToUpdate = getResidentById(residentId);

    residentToUpdate.setFirstName(resident.getFirstName());
    residentToUpdate.setLastName(resident.getLastName());
    residentToUpdate.setPhone(resident.getPhone());

    return residentRepository.save(residentToUpdate);
  }

  public void addInvitationToResident(String residentId, Invitation invitation) {
    Resident resident = getResidentById(residentId);
    resident.addInvitation(invitation);
    residentRepository.save(resident);
  }

  public void deleteResident(String residentId) {
    getResidentById(residentId);
    residentRepository.delete(residentId);
  }

}
