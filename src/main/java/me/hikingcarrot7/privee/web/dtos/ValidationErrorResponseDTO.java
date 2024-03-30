package me.hikingcarrot7.privee.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationErrorResponseDTO {
  @Schema(description = "The host that generated the error", required = true, example = "localhost:8080")
  private String host;

  @Schema(description = "The resource that caused the error", required = true, example = "/login")
  private String resource;

  @Schema(description = "The error message", required = true, example = "Invalid credentials")
  private String message;

  @Schema(description = "The errors that caused the validation error", required = true)
  private List<ConstraintViolationDTO> errors;
}

@Data
@AllArgsConstructor
class ConstraintViolationDTO {
  @Schema(description = "The class that contains the violation", required = true, example = "me.hikingcarrot7.privee.web.dtos.resident.ResidentDTO")
  private String clazz;

  @Schema(description = "The field that contains the violation", required = true, example = "email")
  private String field;

  @Schema(description = "The violation message", required = true, example = "Email is required")
  private String violationMessage;
}
