package me.hikingcarrot7.privee.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "homes")
@Getter
@Setter
public class Home {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @OneToOne(mappedBy = "home")
  private Resident resident;
}
