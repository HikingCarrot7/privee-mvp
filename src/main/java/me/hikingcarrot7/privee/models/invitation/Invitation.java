package me.hikingcarrot7.privee.models.invitation;

import jakarta.persistence.*;
import lombok.*;
import me.hikingcarrot7.privee.models.CloudFile;
import me.hikingcarrot7.privee.models.Resident;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "invitations")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Invitation {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @ToString.Exclude
  private Resident resident;

  @OneToOne(mappedBy = "invitation", cascade = {CascadeType.DETACH, CascadeType.REMOVE})
  @JoinColumn(name = "verification_id", referencedColumnName = "id")
  private InvitationVerification verification;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "expiration_date")
  private LocalDateTime expirationDate;

  @Column(name = "guest_name")
  private String guestName;

  @Column(name = "guest_email")
  private String guestEmail;

  @Column(name = "vehicle_plate")
  private String vehiclePlate;

  @Column(unique = true)
  private String token;

  @Enumerated(EnumType.STRING)
  private InvitationStatus status;

  @Embedded
  @ToString.Exclude
  private CloudFile qrCode;

  @PrePersist
  private void prePersist() {
    createdAt = LocalDateTime.now(ZoneOffset.UTC);
  }

  public boolean isAlreadyVerified() {
    return status == InvitationStatus.VERIFIED;
  }

  public boolean isExpired() {
    return expirationDate.isBefore(LocalDateTime.now(ZoneOffset.UTC));
  }

}
