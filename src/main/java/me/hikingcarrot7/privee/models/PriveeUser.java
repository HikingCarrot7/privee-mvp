package me.hikingcarrot7.privee.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@MappedSuperclass
@Getter
@Setter
public class PriveeUser {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(unique = true)
  private String email;

  private String password;

  @Column(unique = true)
  private String phone;

  @Column(name = "is_deleted")
  private boolean isDeleted = false;

  @PrePersist
  public void prePersist() {
    createdAt = LocalDateTime.now(ZoneOffset.UTC);
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }

}
