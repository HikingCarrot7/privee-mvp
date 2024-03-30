package me.hikingcarrot7.privee.web.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@RequestScoped
@Path("/")
public class OpenApiProxyRestFacade {
  private Client client;

  @Inject
  @ConfigProperty(name = "app.url", defaultValue = "http://localhost:8080")
  private String openApiUrl;

  @PostConstruct
  public void init() {
    this.client = ClientBuilder.newClient();
  }

  @GET
  @Path("/openapi")
  @Produces(MediaType.APPLICATION_JSON)
  public Response proxyOpenApiCall() {
    String entity = client.target(openApiUrl).path("openapi").request().get(String.class);
    return Response.ok(entity).build();
  }

  @PreDestroy
  public void destroy() {
    this.client.close();
  }

}
