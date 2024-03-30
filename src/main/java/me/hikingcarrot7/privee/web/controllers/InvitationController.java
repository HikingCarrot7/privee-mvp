package me.hikingcarrot7.privee.web.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hikingcarrot7.privee.models.invitation.Invitation;
import me.hikingcarrot7.privee.services.InvitationService;
import me.hikingcarrot7.privee.services.ReportService;
import me.hikingcarrot7.privee.utils.PerformanceLog;
import me.hikingcarrot7.privee.web.dtos.ErrorResponseDTO;
import me.hikingcarrot7.privee.web.dtos.InvitationDTO;
import me.hikingcarrot7.privee.web.dtos.InvitationsIntervalParams;
import me.hikingcarrot7.privee.web.dtos.mapper.InvitationMapper;
import me.hikingcarrot7.privee.web.dtos.pagination.PageRequest;
import me.hikingcarrot7.privee.web.utils.ExtraMediaTypes;
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

import java.io.ByteArrayInputStream;
import java.util.List;

import static me.hikingcarrot7.privee.models.PriveeUserRole.*;

@Path("/invitations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Invitations", description = "Operations to manage invitations")
public class InvitationController {
  @Inject private InvitationMapper invitationMapper;
  @Inject private InvitationService invitationService;
  @Inject private ReportService reportService;
  @Inject private JsonWebToken principal;

  @GET
  @Operation(summary = "Get all invitations of a resident")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = InvitationDTO.class)
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
  @RolesAllowed({ADMIN, RESIDENT})
  @PerformanceLog
  public Response getInvitationsHistoryOfResidentAsJson(
      @QueryParam("residentId") String residentId,
      @BeanParam @Valid PageRequest pageRequest
  ) {
    List<Invitation> invitations = invitationService.getInvitationsOfResident(residentId, pageRequest);
    return Response.ok(invitationMapper.toInvitationDTOs(invitations)).build();
  }

  @GET
  @Operation(summary = "Get all invitations of a resident as an Excel file. The file will contain the history of the invitations of the resident in the specified interval; if no interval is specified, it will return all the invitations of the resident.")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = ExtraMediaTypes.APPLICATION_EXCEL_VALUE
          ))
  })
  @Parameters({
      @Parameter(
          name = "residentId",
          description = "The id of the resident",
          example = "742cada2-cbbf-48af-900e-5dee93571684",
          required = true
      ),
      @Parameter(
          name = "startDate",
          description = "The start date of the interval",
          example = "2021-01-01",
          schema = @Schema(type = SchemaType.STRING, format = "date")
      ),
      @Parameter(
          name = "endDate",
          description = "The end date of the interval",
          example = "2021-12-31",
          schema = @Schema(type = SchemaType.STRING, format = "date")
      )
  })
  @Produces(ExtraMediaTypes.APPLICATION_EXCEL_VALUE)
  @RolesAllowed({ADMIN})
  @PerformanceLog
  public Response getInvitationsHistoryOfResidentAsXlsx(
      @QueryParam("residentId") String residentId,
      @BeanParam @Valid InvitationsIntervalParams invitationsIntervalParams
  ) {
    var report = reportService.generateXlsxReportForResidentInvitationHistory(residentId, invitationsIntervalParams);
    return Response.ok(new ByteArrayInputStream(report.reportByteArray()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.fileName() + ".xls\"")
        .type(MediaType.valueOf(ExtraMediaTypes.APPLICATION_EXCEL_VALUE))
        .cacheControl(CacheControl.valueOf("no-cache"))
        .build();
  }

  @GET
  @Path("/{invitationId}")
  @Operation(summary = "Get an invitation by id")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "200",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = InvitationDTO.class)
          ))
  })
  @Parameter(
      name = "invitationId",
      description = "The id of the invitation",
      example = "742cada2-cbbf-48af-900e-5dee93571684",
      required = true
  )
  @RolesAllowed({ADMIN, RESIDENT})
  @PerformanceLog
  public Response getInvitationById(@PathParam("invitationId") String invitationId) {
    Invitation invitation = invitationService.getInvitationById(invitationId);
    return Response.ok(invitationMapper.toInvitationDTO(invitation)).build();
  }

  @POST
  @Operation(summary = "Create an invitation")
  @APIResponses(value = {
      @APIResponse(
          responseCode = "201",
          description = "Successful operation",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = InvitationDTO.class)
          ))
  })
  @RolesAllowed({RESIDENT})
  @PerformanceLog
  public Response createInvitation(
      @Valid @RequestBody(
          description = "The invitation to create",
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = InvitationDTO.class)
          )
      )
      InvitationDTO invitationDTO
  ) {
    String residentId = principal.getClaim("id");
    Invitation invitation = invitationMapper.toInvitation(invitationDTO);
    Invitation result = invitationService.createInvitation(residentId, invitation);
    return Response.ok(invitationMapper.toInvitationDTO(result)).build();
  }

  @POST
  @Path("/validate/{token}")
  @Operation(summary = "Validate an invitation. This will mark the invitation as verified and add a verification to the gatekeeper.")
  @APIResponses(value = {
      @APIResponse(responseCode = "204", description = "Successful operation"),
      @APIResponse(
          responseCode = "404",
          description = "Invitation not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class)
          )
      )
  })
  @Parameter(
      name = "token",
      description = "The token of the invitation",
      example = "742cada2-cbbf-48af-900e-5dee93571684",
      required = true
  )
  @RolesAllowed({GATEKEEPER})
  @PerformanceLog
  public Response validateInvitation(@PathParam("token") String token, @QueryParam("gatekeeperId") String gatekeeperId) {
    invitationService.validateInvitation(token, gatekeeperId);
    return Response.noContent().build();
  }

  @DELETE
  @Path("/{invitationId}")
  @Operation(summary = "Cancel an invitation. This will mark the invitation as canceled.")
  @APIResponses(value = {
      @APIResponse(responseCode = "204", description = "Successful operation"),
      @APIResponse(
          responseCode = "404",
          description = "Invitation not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponseDTO.class)
          )
      )
  })
  @Parameter(
      name = "invitationId",
      description = "The id of the invitation",
      example = "742cada2-cbbf-48af-900e-5dee93571684",
      required = true
  )
  @RolesAllowed({GATEKEEPER})
  @PerformanceLog
  public Response cancelInvitation(@PathParam("invitationId") String invitationId) {
    invitationService.cancelInvitation(invitationId);
    return Response.noContent().build();
  }

}
