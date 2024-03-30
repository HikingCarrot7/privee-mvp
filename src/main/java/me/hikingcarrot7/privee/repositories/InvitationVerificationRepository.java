package me.hikingcarrot7.privee.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import me.hikingcarrot7.privee.models.invitation.InvitationVerification;

@ApplicationScoped
@Transactional
public class InvitationVerificationRepository {
  @Inject private EntityManager em;

  public InvitationVerification save(InvitationVerification verification) {
    em.persist(verification);
    em.flush();
    return verification;
  }

}
