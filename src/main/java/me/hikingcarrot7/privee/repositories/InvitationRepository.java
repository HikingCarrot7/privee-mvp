package me.hikingcarrot7.privee.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import me.hikingcarrot7.privee.models.invitation.Invitation;
import me.hikingcarrot7.privee.web.dtos.InvitationsIntervalParams;
import me.hikingcarrot7.privee.web.dtos.pagination.PageRequest;

import java.util.List;

@ApplicationScoped
@Transactional
public class InvitationRepository {
  @Inject private EntityManager em;

  public List<Invitation> getInvitationsOfResident(String residentId, PageRequest pageRequest) {
    return em.createQuery("SELECT i FROM Invitation i WHERE i.resident.id = :residentId", Invitation.class)
        .setParameter("residentId", residentId)
        .setFirstResult((pageRequest.getPage() - 1) * pageRequest.getSize())
        .setMaxResults(pageRequest.getSize())
        .getResultList();
  }

  public List<Invitation> getAllInvitationsOfResident(String residentId) {
    return em.createQuery("SELECT i FROM Invitation i WHERE i.resident.id = :residentId", Invitation.class)
        .setParameter("residentId", residentId)
        .getResultList();
  }

  public List<Invitation> getInvitationsOnInterval(String residentId, InvitationsIntervalParams intervalParams) {
    if (intervalParams.mustIgnoreInterval()) {
      return getAllInvitationsOfResident(residentId);
    }
    return em.createQuery("SELECT i FROM Invitation i WHERE i.createdAt BETWEEN :startDate AND :endDate", Invitation.class)
        .setParameter("startDate", intervalParams.getStartDate().atStartOfDay())
        .setParameter("endDate", intervalParams.getEndDate().atTime(23, 59, 59))
        .getResultList();
  }

  public List<Invitation> getPendingInvitations() {
    return em.createQuery("SELECT i FROM Invitation i WHERE i.status = 'PENDING'", Invitation.class)
        .getResultList();
  }

  public Invitation findById(String id) {
    return em.find(Invitation.class, id);
  }

  public Invitation findByToken(String token) {
    return em.createQuery("SELECT i FROM Invitation i WHERE i.token = :token", Invitation.class)
        .setParameter("token", token)
        .getSingleResult();
  }

  public Invitation save(Invitation invitation) {
    em.persist(invitation);
    em.flush();
    return invitation;
  }

}
