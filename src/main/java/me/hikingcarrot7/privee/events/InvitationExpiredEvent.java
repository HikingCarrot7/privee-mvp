package me.hikingcarrot7.privee.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.hikingcarrot7.privee.models.Resident;

@Data
@AllArgsConstructor
public class InvitationExpiredEvent {
  private Resident resident;
}
