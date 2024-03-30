package me.hikingcarrot7.privee.models;

import jakarta.persistence.*;
import lombok.*;
import me.hikingcarrot7.privee.models.invitation.Invitation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "residents")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Resident extends PriveeUser {
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "home_id", referencedColumnName = "id")
  private Home home;

  @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  private List<Invitation> invitations;

  public void addInvitation(Invitation invitation) {
    if (Objects.isNull(invitations)) {
      invitations = new ArrayList<>();
    }
    invitations.add(invitation);
    invitation.setResident(this);
  }

}



