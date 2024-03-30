package me.hikingcarrot7.privee.web.dtos.gatekeeper;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.hikingcarrot7.privee.web.dtos.validation.Password;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GatekeeperDTO {
  private String id;

  @NotBlank(message = "First name is required")
  @Schema(description = "Gatekeeper's first name", required = true, example = "Gerardo Nicolás")
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Schema(description = "Gatekeeper's last name", required = true, example = "Canul Ibarra")
  private String lastName;

  @NotBlank(message = "Phone number is required")
  @Schema(description = "Gatekeeper's phone number", required = true, example = "9991234567")
  private String phone;

  @Email
  @NotBlank(message = "Email is required")
  @Schema(description = "Gatekeeper's email", required = true, example = "ricardoibarra2044@gmai.com")
  private String email;

  @Password
  @NotBlank
  @Schema(description = "Gatekeeper's password", required = true, example = "password123")
  private String password;
}
