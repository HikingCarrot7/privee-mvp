package me.hikingcarrot7.privee.web.dtos.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.hikingcarrot7.privee.web.dtos.validation.Password;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDTO {
  @Schema(hidden = true)
  private String id;

  @Schema(hidden = true)
  private LocalDateTime createdAt;

  @NotBlank(message = "First name is required")
  @Schema(description = "Admin's first name", required = true, example = "Ricardo Nicolás")
  private String firstName;

  @Schema(description = "Admin's last name", required = true, example = "Canul Ibarra")
  @NotBlank(message = "Last name is required")
  private String lastName;

  @NotBlank(message = "Phone is required")
  @Schema(description = "Admin's phone number", example = "9991234567")
  private String phone;

  @Email
  @NotBlank(message = "Email is required")
  @Schema(description = "Admin's email", required = true, example = "ricardoibarra2044@gmail.com")
  private String email;

  @Password
  @NotBlank(message = "Password is required")
  @Schema(description = "Admin's password", required = true, example = "password123")
  private String password;
}
