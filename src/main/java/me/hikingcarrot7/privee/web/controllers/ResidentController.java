package me.hikingcarrot7.privee.web.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import me.hikingcarrot7.privee.models.Resident;
import me.hikingcarrot7.privee.services.ResidentService;
import me.hikingcarrot7.privee.utils.PerformanceLog;
import me.hikingcarrot7.privee.web.dtos.ErrorResponseDTO;
import me.hikingcarrot7.privee.web.dtos.ValidationErrorResponseDTO;
import me.hikingcarrot7.privee.web.dtos.mapper.ResidentMapper;
import me.hikingcarrot7.privee.web.dtos.pagination.PageRequest;
import me.hikingcarrot7.privee.web.dtos.resident.ResidentDTO;
import me.hikingcarrot7.privee.web.dtos.resident.UpdateResidentDTO;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

import static me.hikingcarrot7.privee.models.PriveeUserRole.ADMIN;
import static me.hikingcarrot7.privee.models.PriveeUserRole.RESIDENT;

@Path("/residents")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Residents", description = "Operations to manage residents")
public class ResidentController {
  @Inject private ResidentMapper residentMapper;
  @Inject private ResidentService residentService;
  @Inject private JsonWebToken principal;

  @GET
  @Operation(summary = "Get all residents")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ResidentDTO.class)
          ))
  })
  @Parameters({
      @Parameter(
          name = "page",
          description = "The page number to get",
          example = "1",
          schema = @Schema(type = SchemaType.INTEGER)
      ),
      @Parameter(
          name = "size",
          description = "The number of elements per page",
          example = "10",
          schema = @Schema(type = SchemaType.INTEGER)
      )
  })
  @RolesAllowed({ADMIN})
  @PerformanceLog
  public Response getAllResidents(@BeanParam PageRequest pageRequest) {
    List<Resident> allResidents = residentService.getResidents(pageRequest);
    return Response
        .ok(residentMapper.toResidentDTOs(allResidents))
        .build();
  }

  @GET
  @Path("/{residentId}")
  @Operation(summary = "Get a resident by id")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ResidentDTO.class)
          )),
      @APIResponse(
          responseCode = "404",
          description = "Resident not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class)
          )
      )
  })
  @Parameter(
      name = "residentId",
      description = "The UUID of the resident to get",
      example = "742cada2-cbbf-48af-900e-5dee93571684",
      schema = @Schema(type = SchemaType.STRING)
  )
  @RolesAllowed({ADMIN, RESIDENT})
  @PerformanceLog
  public Response getResidentById(@PathParam("residentId") String residentId) {
    Resident resident = residentService.getResidentById(residentId);
    return Response.ok(residentMapper.toResidentDTO(resident)).build();
  }

  @GET
  @Path("/me")
  @Operation(summary = "Get the logged resident")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ResidentDTO.class)
          )
      ),
      @APIResponse(
          responseCode = "404",
          description = "Resident not found, if the resident is not logged in",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class)
          )
      )
  })
  @RolesAllowed({RESIDENT})
  @PerformanceLog
  public Response getLoggedResident() {
    String residentId = principal.getClaim("id");
    Resident resident = residentService.getResidentById(residentId);
    return Response.ok(residentMapper.toResidentDTO(resident)).build();
  }

  @POST
  @Operation(summary = "Save a resident")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "201",
          description = "Resident created",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ResidentDTO.class)
          )),
      @APIResponse(
          responseCode = "400",
          description = "Invalid request body",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ValidationErrorResponseDTO.class)
          )
      )
  })
  @RolesAllowed({ADMIN})
  @PerformanceLog
  public Response saveResident(
      @Valid @RequestBody(
          description = "Resident to save",
          content = @Content(schema = @Schema(implementation = ResidentDTO.class)),
          required = true
      )
      ResidentDTO residentDto,

      @Context
      UriInfo uriInfo
  ) {
    Resident resident = residentMapper.toResident(residentDto);
    Resident result = residentService.saveResident(resident);
    return Response
        .created(uriInfo.getAbsolutePathBuilder().path(result.getId()).build())
        .entity(residentMapper.toResidentDTO(result))
        .build();
  }

  @PUT
  @Path("/{residentId}")
  @Operation(summary = "Update a resident")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "200",
          description = "Resident updated",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = UpdateResidentDTO.class)
          )
      ),
      @APIResponse(
          responseCode = "404",
          description = "Resident not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class)
          )
      )
  })
  @RolesAllowed({ADMIN, RESIDENT})
  @PerformanceLog
  public Response updateResident(
      @Valid @RequestBody(
          description = "Resident to update",
          content = @Content(schema = @Schema(implementation = ResidentDTO.class)),
          required = true
      )
      UpdateResidentDTO updateResidentDto,

      @PathParam("residentId")
      String residentId
  ) {
    Resident resident = residentMapper.toResident(updateResidentDto);
    Resident result = residentService.updateResident(residentId, resident);
    return Response.ok(residentMapper.toResidentDTO(result)).build();
  }

  @DELETE
  @Path("/{residentId}")
  @Operation(summary = "Delete a resident")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "204",
          description = "Resident deleted"
      ),
      @APIResponse(
          responseCode = "404",
          description = "Resident not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class)
          )
      )
  })
  @RolesAllowed({ADMIN})
  @PerformanceLog
  public Response deleteResident(@PathParam("residentId") String residentId) {
    residentService.deleteResident(residentId);
    return Response.noContent().build();
  }

}
