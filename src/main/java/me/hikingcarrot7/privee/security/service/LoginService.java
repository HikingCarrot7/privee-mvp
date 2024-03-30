package me.hikingcarrot7.privee.security.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.hikingcarrot7.privee.models.Gatekeeper;
import me.hikingcarrot7.privee.models.PriveeUser;
import me.hikingcarrot7.privee.models.PriveeUserRole;
import me.hikingcarrot7.privee.models.Resident;
import me.hikingcarrot7.privee.security.JwtTokenGenerator;
import me.hikingcarrot7.privee.security.SecurityUser;
import me.hikingcarrot7.privee.services.AdminService;
import me.hikingcarrot7.privee.services.GatekeeperService;
import me.hikingcarrot7.privee.services.ResidentService;
import me.hikingcarrot7.privee.services.exceptions.InvalidCredentialsException;
import me.hikingcarrot7.privee.utils.PasswordEncoder;
import me.hikingcarrot7.privee.web.dtos.auth.LoginRequestDTO;
import me.hikingcarrot7.privee.web.dtos.auth.TokenDTO;

@ApplicationScoped
public class LoginService {
  @Inject private JwtTokenGenerator jwtTokenGenerator;
  @Inject private PasswordEncoder passwordEncoder;
  @Inject private ResidentService residentService;
  @Inject private GatekeeperService gatekeeperService;
  @Inject private AdminService adminService;

  public TokenDTO loginResident(LoginRequestDTO loginRequest) {
    String email = loginRequest.getEmail();
    Resident resident = residentService.getResidentByEmail(email);
    if (passwordEncoder.matches(loginRequest.getPassword(), resident.getPassword())) {
      return generateTokenForResident(resident);
    }
    throw new InvalidCredentialsException();
  }

  public TokenDTO loginGatekeeper(LoginRequestDTO loginRequest) {
    String email = loginRequest.getEmail();
    Gatekeeper gatekeeper = gatekeeperService.getGatekeeperByEmail(email);
    if (passwordEncoder.matches(loginRequest.getPassword(), gatekeeper.getPassword())) {
      return generateTokenForGatekeeper(gatekeeper);
    }
    throw new InvalidCredentialsException();
  }

  public TokenDTO loginAdmin(LoginRequestDTO loginRequest) {
    String email = loginRequest.getEmail();
    PriveeUser admin = adminService.getAdminByEmail(email);
    if (passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
      return generateTokenForAdmin(admin);
    }
    throw new InvalidCredentialsException();
  }

  public TokenDTO generateTokenForResident(Resident resident) {
    return generateTokenForPriveeUser(resident, PriveeUserRole.RESIDENT);
  }

  public TokenDTO generateTokenForGatekeeper(PriveeUser gatekeeper) {
    return generateTokenForPriveeUser(gatekeeper, PriveeUserRole.GATEKEEPER);
  }

  public TokenDTO generateTokenForAdmin(PriveeUser admin) {
    return generateTokenForPriveeUser(admin, PriveeUserRole.ADMIN);
  }

  private TokenDTO generateTokenForPriveeUser(PriveeUser user, String role) {
    SecurityUser securityUser = SecurityUser.builder()
        .id(user.getId())
        .email(user.getEmail())
        .role(role)
        .build();
    String jwtAccessToken = jwtTokenGenerator.generateJWTString(securityUser);
    return TokenDTO.builder()
        .id(user.getId())
        .email(user.getEmail())
        .accessToken(jwtAccessToken)
        .build();
  }

}
