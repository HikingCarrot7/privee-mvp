package me.hikingcarrot7.privee.config;

import com.cloudinary.Cloudinary;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class CloudinaryConfig {
  @Inject
  @ConfigProperty(name = "cloudinary.name")
  private String cloudName;

  @Inject
  @ConfigProperty(name = "cloudinary.api.key")
  private String apiKey;

  @Inject
  @ConfigProperty(name = "cloudinary.api.secret")
  private String apiSecret;

  @Produces
  @Default
  @Singleton
  public Cloudinary cloudinary() {
    Map<String, String> config = new HashMap<>();
    config.put("cloud_name", cloudName);
    config.put("api_key", apiKey);
    config.put("api_secret", apiSecret);
    return new Cloudinary(config);
  }

}
