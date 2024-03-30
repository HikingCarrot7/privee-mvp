package me.hikingcarrot7.privee.web.utils;

import org.eclipse.microprofile.openapi.OASFactory;
import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.info.Info;
import org.eclipse.microprofile.openapi.models.info.License;
import org.eclipse.microprofile.openapi.models.responses.APIResponse;

import java.util.Collections;

public class PriveeOASFilter implements OASFilter {

  @Override
  public APIResponse filterAPIResponse(APIResponse apiResponse) {
    if ("Missing description".equals(apiResponse.getDescription())) {
      apiResponse.setDescription("Invalid hostname or the system service may not be running on the particular host.");
    }
    return apiResponse;
  }

  @Override
  public void filterOpenAPI(OpenAPI openAPI) {
    openAPI.setInfo(
        OASFactory.createObject(Info.class).title("Privee backend").version("1.0")
            .description("Privee is a simple application to manage access to private residential areas.")
            .license(
                OASFactory.createObject(License.class)
                    .name("Apache License - v 2.0, January 2004")
                    .url("http://www.apache.org/licenses/"))
    );

    openAPI.addServer(
        OASFactory.createServer()
            .url("http://localhost:{port}")
            .description("Local server for development purposes")
            .variables(Collections.singletonMap("port",
                OASFactory.createServerVariable()
                    .defaultValue("8080")
                    .description("Server HTTP port."))
            )
    );
  }
}
