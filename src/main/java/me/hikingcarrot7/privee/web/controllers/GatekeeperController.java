package me.hikingcarrot7.privee.web.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import me.hikingcarrot7.privee.models.Gatekeeper;
import me.hikingcarrot7.privee.services.GatekeeperService;
import me.hikingcarrot7.privee.utils.PerformanceLog;
import me.hikingcarrot7.privee.web.dtos.ErrorResponseDTO;
import me.hikingcarrot7.privee.web.dtos.ValidationErrorResponseDTO;
import me.hikingcarrot7.privee.web.dtos.gatekeeper.GatekeeperDTO;
import me.hikingcarrot7.privee.web.dtos.gatekeeper.UpdateGatekeeperDTO;
import me.hikingcarrot7.privee.web.dtos.mapper.GatekeeperMapper;
import me.hikingcarrot7.privee.web.dtos.pagination.PageRequest;
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
import static me.hikingcarrot7.privee.models.PriveeUserRole.GATEKEEPER;

@Path("/gatekeepers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Gatekeepers", description = "Operations to manage gatekeepers")
public class GatekeeperController {
  @Inject private GatekeeperMapper gatekeeperMapper;
  @Inject private GatekeeperService gatekeeperService;
  @Inject private JsonWebToken principal;

  @GET
  @Operation(summary = "Get all gatekeepers")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = GatekeeperDTO.class)
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
  public Response getAllGatekeepers(@BeanParam @Valid PageRequest pageRequest) {
    List<Gatekeeper> allGatekeepers = gatekeeperService.getAllGatekeepers(pageRequest);
    return Response.ok(gatekeeperMapper.toGatekeeperDTOs(allGatekeepers)).build();
  }

  @GET
  @Path("/{gatekeeperId}")
  @Operation(summary = "Get a gatekeeper by id")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = GatekeeperDTO.class)
          )),
      @APIResponse(
          responseCode = "404",
          description = "Gatekeeper not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class)
          )
      )
  })
  @Parameter(
      name = "gatekeeperId",
      description = "The id of the gatekeeper",
      required = true,
      example = "8e26211a-c06a-4afb-9803-69c2d0fce275"
  )
  @RolesAllowed({ADMIN, GATEKEEPER})
  @PerformanceLog
  public Response getGatekeeperById(@PathParam("gatekeeperId") String id) {
    Gatekeeper gatekeeper = gatekeeperService.getGatekeeperById(id);
    return Response.ok(gatekeeperMapper.toGatekeeperDTO(gatekeeper)).build();
  }

  @GET
  @Path("/me")
  @Operation(summary = "Get the logged gatekeeper")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = GatekeeperDTO.class)
          )),
      @APIResponse(
          responseCode = "404",
          description = "Gatekeeper not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class)
          )
      )
  })
  @RolesAllowed({GATEKEEPER})
  @PerformanceLog
  public Response getLoggedGatekeeper() {
    String gatekeeperId = principal.getClaim("id");
    Gatekeeper gatekeeper = gatekeeperService.getGatekeeperById(gatekeeperId);
    return Response.ok(gatekeeperMapper.toGatekeeperDTO(gatekeeper)).build();
  }

  @POST
  @Operation(summary = "Create a gatekeeper")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "201",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = GatekeeperDTO.class)
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
  public Response createGatekeeper(
      @Valid @RequestBody(
          description = "The gatekeeper to create",
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = GatekeeperDTO.class)
          )
      )
      GatekeeperDTO gatekeeperDTO,

      @Context
      UriInfo uriInfo
  ) {
    Gatekeeper gatekeeper = gatekeeperMapper.toGatekeeper(gatekeeperDTO);
    Gatekeeper result = gatekeeperService.saveGatekeeper(gatekeeper);
    return Response.created(uriInfo.getAbsolutePathBuilder().path(result.getId()).build())
        .entity(gatekeeperMapper.toGatekeeperDTO(result))
        .build();
  }

  @PUT
  @Path("/{gatekeeperId}")
  @Operation(summary = "Update a gatekeeper")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = GatekeeperDTO.class)
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
  public Response updateGatekeeper(
      @PathParam("gatekeeperId") String gatekeeperId,
      @Valid @RequestBody(
          description = "The gatekeeper to update",
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = UpdateResidentDTO.class)
          )
      )
      UpdateGatekeeperDTO updateGatekeeperDto
  ) {
    Gatekeeper gatekeeper = gatekeeperMapper.toGatekeeper(updateGatekeeperDto);
    Gatekeeper result = gatekeeperService.updateGatekeeper(gatekeeperId, gatekeeper);
    return Response.ok(gatekeeperMapper.toGatekeeperDTO(result)).build();
  }

  @DELETE
  @Path("/{gatekeeperId}")
  @Operation(summary = "Delete a gatekeeper")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "204",
          description = "Gatekeeper deleted"
      )
  })
  @RolesAllowed({ADMIN})
  @PerformanceLog
  public Response deleteGatekeeper(@PathParam("gatekeeperId") String gatekeeperId) {
    gatekeeperService.deleteGatekeeper(gatekeeperId);
    return Response.noContent().build();
  }

}
