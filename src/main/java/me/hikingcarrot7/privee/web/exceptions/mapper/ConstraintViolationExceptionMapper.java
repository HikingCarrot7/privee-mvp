package me.hikingcarrot7.privee.web.exceptions.mapper;

import jakarta.annotation.Priority;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Set;

@Provider
@Priority(Priorities.USER)
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
  @Context private UriInfo uriInfo;

  @Override
  public Response toResponse(final ConstraintViolationException exception) {
    Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
    final var jsonObject = Json.createObjectBuilder()
        .add("host", uriInfo.getAbsolutePath().getHost())
        .add("resource", uriInfo.getAbsolutePath().getPath())
        .add("message", "Validation Errors");

    final var jsonArray = Json.createArrayBuilder();
    JsonObject jsonError = null;
    for (final var constraint : constraintViolations) {
      String className = constraint.getLeafBean().toString().split("@")[0];
      String message = constraint.getMessage();
      String propertyPath = constraint.getPropertyPath().toString().split("\\.")[2];

      jsonError = Json.createObjectBuilder()
          .add("class", className)
          .add("field", propertyPath)
          .add("violationMessage", message)
          .build();
      jsonArray.add(jsonError);
    }

    JsonObject errorJsonEntity = jsonObject.add("errors", jsonArray.build()).build();
    return Response.status(Response.Status.BAD_REQUEST).entity(errorJsonEntity).build();
  }

}
