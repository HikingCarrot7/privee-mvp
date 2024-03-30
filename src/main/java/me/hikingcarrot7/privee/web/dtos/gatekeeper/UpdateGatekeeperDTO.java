package me.hikingcarrot7.privee.web.dtos.gatekeeper;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class UpdateGatekeeperDTO {

  @Schema(description = "Gatekeeper's first name", required = true, example = "Gerardo Nicolás")
  private String firstName;

  @Schema(description = "Gatekeeper's last name", required = true, example = "Canul Ibarra")
  private String lastName;

  @Schema(description = "Gatekeeper's phone number", required = true, example = "9991234567")
  private String phone;
}
