package me.hikingcarrot7.privee.models.invitation;

import jakarta.persistence.*;
import lombok.*;
import me.hikingcarrot7.privee.models.Gatekeeper;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitation_verification")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InvitationVerification {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "verified_at")
  private LocalDateTime verifiedAt;

  @OneToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Invitation invitation;

  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Gatekeeper verifiedBy;

  @PrePersist
  private void prePersist() {
    verifiedAt = LocalDateTime.now();
  }

}
