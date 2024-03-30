package me.hikingcarrot7.privee.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hikingcarrot7.privee.models.invitation.InvitationVerification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "gatekeepers")
@Getter
@Setter
@ToString
public class Gatekeeper extends PriveeUser {

  @OneToMany(mappedBy = "verifiedBy")
  @ToString.Exclude
  private List<InvitationVerification> verifications;

  public void addVerification(InvitationVerification verification) {
    if (Objects.isNull(verifications)) {
      verifications = new ArrayList<>();
    }
    verifications.add(verification);
    verification.setVerifiedBy(this);
  }

}
