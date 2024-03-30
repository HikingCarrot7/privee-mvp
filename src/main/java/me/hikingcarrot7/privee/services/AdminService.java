package me.hikingcarrot7.privee.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.hikingcarrot7.privee.models.Admin;
import me.hikingcarrot7.privee.repositories.AdminRepository;
import me.hikingcarrot7.privee.services.exceptions.ResourceNotFoundException;
import me.hikingcarrot7.privee.utils.PasswordEncoder;
import me.hikingcarrot7.privee.web.dtos.pagination.PageRequest;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AdminService {
  @Inject private AdminRepository adminRepository;
  @Inject private PasswordEncoder passwordEncoder;

  public List<Admin> getAdmins(PageRequest pageRequest) {
    return adminRepository.findAll(pageRequest);
  }

  public Admin getAdminById(String adminId) {
    Optional<Admin> admin = adminRepository.findById(adminId);
    return admin.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Admin with adminId %s not found", adminId))
    );
  }

  public Admin getAdminByEmail(String email) {
    Optional<Admin> admin = adminRepository.findByEmail(email);
    return admin.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Admin with email %s not found", email))
    );
  }

  public Admin saveAdmin(Admin admin) {
    admin.setPassword(passwordEncoder.encode(admin.getPassword()));
    return adminRepository.save(admin);
  }

  public Admin updateAdmin(String adminId, Admin admin) {
    Admin adminToUpdate = getAdminById(adminId);

    adminToUpdate.setFirstName(admin.getFirstName());
    adminToUpdate.setLastName(admin.getLastName());
    adminToUpdate.setPhone(admin.getPhone());

    return adminRepository.save(adminToUpdate);
  }

  public void deleteAdmin(String adminId) {
    getAdminById(adminId);
    adminRepository.delete(adminId);
  }


}
