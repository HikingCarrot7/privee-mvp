package me.hikingcarrot7.privee.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Objects;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CloudFile {
  @Column
  private String publicId;

  @Column
  private String url;

  @Column
  private String originalFilename;

  public boolean isValidFile() {
    return Objects.nonNull(publicId) && Objects.nonNull(url);
  }

}
