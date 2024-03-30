package me.hikingcarrot7.privee.web.dtos.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO {
  private String id;
  private String email;
  private String name;
  private String accessToken;
}
