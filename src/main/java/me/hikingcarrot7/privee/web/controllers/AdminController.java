package me.hikingcarrot7.privee.web.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hikingcarrot7.privee.models.Admin;
import me.hikingcarrot7.privee.services.AdminService;
import me.hikingcarrot7.privee.utils.PerformanceLog;
import me.hikingcarrot7.privee.web.dtos.ErrorResponseDTO;
import me.hikingcarrot7.privee.web.dtos.admin.AdminDTO;
import me.hikingcarrot7.privee.web.dtos.admin.UpdateAdminDTO;
import me.hikingcarrot7.privee.web.dtos.mapper.AdminMapper;
import me.hikingcarrot7.privee.web.dtos.pagination.PageRequest;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

import static me.hikingcarrot7.privee.models.PriveeUserRole.ADMIN;

@Path("/admins")
@Tag(name = "Admins", description = "Operations to manage admins")
public class AdminController {
  @Inject private AdminService adminService;
  @Inject private AdminMapper adminMapper;
  @Inject private JsonWebToken principal;

  @GET
  @Operation(summary = "Get all admins")
  @APIResponses({
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = AdminDTO.class)
          )
      )
  })
  @RolesAllowed({ADMIN})
  @PerformanceLog
  public Response getAdmins(@BeanParam PageRequest pageRequest) {
    List<Admin> admins = adminService.getAdmins(pageRequest);
    List<AdminDTO> adminDTOS = adminMapper.toAdminDTOs(admins);
    return Response.ok(adminDTOS).build();
  }

  @GET
  @Path("/{adminId}")
  @Operation(summary = "Get an admin by id")
  @APIResponses({
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = AdminDTO.class)
          )
      ),
      @APIResponse(
          responseCode = "404",
          description = "Admin not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class)
          )
      )
  })
  public Response getAdminById(@PathParam("adminId") String adminId) {
    Admin admin = adminService.getAdminById(adminId);
    AdminDTO adminDTO = adminMapper.toAdminDTO(admin);
    return Response.ok(adminDTO).build();
  }

  @GET
  @Path("/me")
  @Operation(summary = "Get the current logged admin")
  @APIResponses({
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = AdminDTO.class)
          )
      ),
      @APIResponse(
          responseCode = "404",
          description = "Admin not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class)
          )
      )
  })
  @RolesAllowed({ADMIN})
  @PerformanceLog
  public Response getLoggedAdmin() {
    String adminId = principal.getClaim("id");
    Admin admin = adminService.getAdminById(adminId);
    AdminDTO adminDTO = adminMapper.toAdminDTO(admin);
    return Response.ok(adminDTO).build();
  }

  @POST
  @Operation(summary = "Create an admin")
  @APIResponses({
      @APIResponse(
          responseCode = "201",
          description = "Admin created",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = AdminDTO.class)
          )
      ),
      @APIResponse(
          responseCode = "400",
          description = "Invalid admin",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class)
          )
      )
  })
  @RolesAllowed({ADMIN})
  @PerformanceLog
  public Response saveAdmin(@Valid AdminDTO adminDTO) {
    Admin admin = adminMapper.toAdmin(adminDTO);
    Admin result = adminService.saveAdmin(admin);
    AdminDTO resultDTO = adminMapper.toAdminDTO(result);
    return Response.ok(resultDTO).build();
  }

  @PUT
  @Path("/{adminId}")
  @Operation(summary = "Update an admin")
  @APIResponses({
      @APIResponse(
          responseCode = "200",
          description = "Admin updated",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = AdminDTO.class)
          )
      ),
      @APIResponse(
          responseCode = "400",
          description = "Invalid admin",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class)
          )
      )
  })
  @Parameters({
      @Parameter(
          name = "adminId",
          description = "The id of the admin",
          example = "742cada2-cbbf-48af-900e-5dee93571684",
          required = true
      )
  })
  @RolesAllowed({ADMIN})
  @PerformanceLog
  public Response updateAdmin(
      @PathParam("adminId") String adminId,
      @Valid UpdateAdminDTO updateAdminDTO
  ) {
    Admin admin = adminMapper.toAdmin(updateAdminDTO);
    Admin result = adminService.updateAdmin(adminId, admin);
    AdminDTO resultDTO = adminMapper.toAdminDTO(result);
    return Response.ok(resultDTO).build();
  }

  @DELETE
  @Path("/{adminId}")
  @Operation(summary = "Delete an admin")
  @APIResponses({
      @APIResponse(
          responseCode = "204",
          description = "Admin deleted"
      ),
      @APIResponse(
          responseCode = "404",
          description = "Admin not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class)
          )
      )
  })
  @Parameters({
      @Parameter(
          name = "adminId",
          description = "The id of the admin",
          example = "742cada2-cbbf-48af-900e-5dee93571684",
          required = true
      )
  })
  @RolesAllowed({ADMIN})
  @PerformanceLog
  public Response deleteAdmin(@PathParam("adminId") String adminId) {
    adminService.deleteAdmin(adminId);
    return Response.noContent().build();
  }

}
