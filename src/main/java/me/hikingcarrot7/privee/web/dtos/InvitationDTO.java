package me.hikingcarrot7.privee.web.dtos;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.hikingcarrot7.privee.models.CloudFile;
import me.hikingcarrot7.privee.models.invitation.InvitationStatus;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitationDTO {
  @Schema(name = "id", description = "The invitation's id")
  private String id;

  @Schema(name = "createdAt", description = "The invitation's creation date")
  private LocalDateTime createdAt;

  @FutureOrPresent(message = "The expiration date must be in the future or present")
  @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss")
  @Schema(description = "The invitation's expiration date", required = true, example = "2021-12-31 23:59:59")
  private LocalDateTime expirationDate;

  @NotBlank(message = "The guest name must not be blank")
  @Schema(description = "The guest's name", required = true, example = "Ricardo Nicolás")
  private String guestName;

  @NotBlank(message = "The guest email must not be blank")
  @Schema(description = "The guest's email", required = true, example = "ricardoibarra2044@gmail.com")
  private String guestEmail;

  @NotBlank(message = "The vehicle plate must not be blank")
  @Schema(description = "The guest's vehicle plate", required = true, example = "YUC-1234")
  private String vehiclePlate;

  @Schema(description = "The invitation's token")
  private String token;

  @Schema(description = "The invitation's status")
  private InvitationStatus status;

  @Schema(description = "The invitation's QR code")
  private CloudFile qrCode;
}
