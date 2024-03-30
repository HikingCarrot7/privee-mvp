package me.hikingcarrot7.privee.config;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

import static me.hikingcarrot7.privee.models.PriveeUserRole.ADMIN;
import static me.hikingcarrot7.privee.models.PriveeUserRole.RESIDENT;

@ApplicationPath("/api")
@LoginConfig(authMethod = "MP-JWT")
@RolesAllowed({ADMIN, RESIDENT})
public class ApplicationConfig extends Application {

}
