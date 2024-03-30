package me.hikingcarrot7.privee.web.dtos.resident;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class UpdateResidentDTO {

  @NotBlank(message = "First name is required")
  @Schema(description = "Resident's first name", required = true, example = "Ricardo Nicolás")
  private String firstName;

  @Schema(description = "Resident's last name", required = true, example = "Canul Ibarra")
  @NotBlank(message = "Last name is required")
  private String lastName;

}
