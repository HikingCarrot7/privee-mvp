package me.hikingcarrot7.privee.web.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import me.hikingcarrot7.privee.web.dtos.validation.Password;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class LoginRequestDTO {
  @Email
  @NotBlank(message = "Email is required")
  @Schema(description = "User's email", required = true, example = "ricardoibarra2044@gmail.com")
  private String email;

  @Password
  @NotBlank(message = "Password is required")
  @Schema(description = "User's password", required = true, example = "password123")
  private String password;
}
