package me.hikingcarrot7.privee.web.controllers;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hikingcarrot7.privee.security.service.LoginService;
import me.hikingcarrot7.privee.utils.PerformanceLog;
import me.hikingcarrot7.privee.web.dtos.ErrorResponseDTO;
import me.hikingcarrot7.privee.web.dtos.auth.LoginRequestDTO;
import me.hikingcarrot7.privee.web.dtos.auth.TokenDTO;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/login")
@PermitAll
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Login", description = "Operations to login for residents, gatekeepers and admins")
public class LoginController {
  @Inject private LoginService loginService;

  @POST
  @Path("/resident")
  @Operation(summary = "Login a resident")
  @APIResponses({
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = TokenDTO.class))
      ),
      @APIResponse(
          responseCode = "400",
          description = "Invalid credentials",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class))
      )
  })
  @PerformanceLog
  public Response loginResident(
      @Valid @RequestBody(
          description = "The login request",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = LoginRequestDTO.class)
          )
      )
      LoginRequestDTO loginRequestDto
  ) {
    TokenDTO token = loginService.loginResident(loginRequestDto);
    return Response.ok(token).build();
  }

  @POST
  @Path("/gatekeeper")
  @Operation(summary = "Login a gatekeeper")
  @APIResponses({
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = TokenDTO.class))
      ),
      @APIResponse(
          responseCode = "400",
          description = "Invalid credentials",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class))
      )
  })
  @PerformanceLog
  public Response loginGatekeeper(
      @Valid @RequestBody(
          description = "The login request",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = LoginRequestDTO.class)
          )
      )
      LoginRequestDTO loginRequestDto
  ) {
    TokenDTO token = loginService.loginGatekeeper(loginRequestDto);
    return Response.ok(token).build();
  }

  @POST
  @Path("/admin")
  @Operation(summary = "Login an admin")
  @APIResponses({
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = TokenDTO.class))
      ),
      @APIResponse(
          responseCode = "400",
          description = "Invalid credentials",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class))
      )
  })
  @PerformanceLog
  public Response loginAdmin(
      @Valid @RequestBody(
          description = "The login request",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = LoginRequestDTO.class)
          )
      )
      LoginRequestDTO loginRequestDto
  ) {
    TokenDTO token = loginService.loginAdmin(loginRequestDto);
    return Response.ok(token).build();
  }


}
