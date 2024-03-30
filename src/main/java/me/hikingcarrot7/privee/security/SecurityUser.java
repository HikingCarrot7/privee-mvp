package me.hikingcarrot7.privee.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SecurityUser {
  private String id;
  private String email;
  private String role;
}
