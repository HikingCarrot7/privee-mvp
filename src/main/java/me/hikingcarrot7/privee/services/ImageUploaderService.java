package me.hikingcarrot7.privee.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.hikingcarrot7.privee.models.CloudFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@ApplicationScoped
public class ImageUploaderService {
  @Inject private Cloudinary cloudinary;

  public CloudFile uploadImage(BufferedImage image, String token) {
    Map<?, ?> options = ObjectUtils.asMap(
        "folder", "privee",
        "resource_type", "auto",
        "use_filename", true,
        "unique_filename", true
    );
    return uploadImage(image, token, options);
  }

  private CloudFile uploadImage(BufferedImage image, String token, Map<?, ?> options) {
    try {
      return tryUploadImage(image, token, options);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private CloudFile tryUploadImage(BufferedImage image, String token, Map<?, ?> options) throws IOException {
    String fileName = String.format("qr-code-%s", token);
    // Create a temporary image to upload to cloudinary
    File tempFile = File.createTempFile(getFileNameWithoutExtension(fileName), getFileExtension(fileName));
    ImageIO.write(image, "jpg", tempFile);
    var result = cloudinary.uploader().upload(tempFile, options);
    String publicId = (String) result.get("public_id");
    String url = (String) result.get("secure_url");
    tempFile.delete();
    return new CloudFile(publicId, url, fileName);
  }

  private String getFileNameWithoutExtension(String fileName) {
    int lastIndexOf = fileName.lastIndexOf(".");
    if (lastIndexOf == -1) {
      return fileName;
    }
    return fileName.substring(0, lastIndexOf);
  }

  private String getFileExtension(String fileName) {
    int lastIndexOf = fileName.lastIndexOf(".");
    if (lastIndexOf == -1) {
      return "";
    }
    return fileName.substring(lastIndexOf);
  }

}
