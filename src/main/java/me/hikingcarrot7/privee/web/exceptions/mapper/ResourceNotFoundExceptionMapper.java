package me.hikingcarrot7.privee.web.exceptions.mapper;

import jakarta.annotation.Priority;
import jakarta.json.Json;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import me.hikingcarrot7.privee.services.exceptions.ResourceNotFoundException;

@Provider
@Priority(Priorities.USER)
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {
  @Context private UriInfo uriInfo;

  @Override
  public Response toResponse(ResourceNotFoundException exception) {
    final var jsonObject = Json.createObjectBuilder()
        .add("host", uriInfo.getAbsolutePath().getHost())
        .add("resource", uriInfo.getAbsolutePath().getPath())
        .add("message", exception.getMessage());
    return Response.status(Response.Status.NOT_FOUND).entity(jsonObject.build()).build();
  }

}
