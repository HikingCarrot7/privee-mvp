package me.hikingcarrot7.privee.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {
  @Schema(description = "The host where the error was caused", required = true, example = "http://localhost:8080")
  private String host;

  @Schema(description = "The resource that caused the error", required = true, example = "/login")
  private String resource;

  @Schema(description = "The error message", required = true, example = "Invalid credentials")
  private String message;
}
