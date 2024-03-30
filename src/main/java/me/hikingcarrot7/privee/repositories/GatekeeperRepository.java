package me.hikingcarrot7.privee.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import me.hikingcarrot7.privee.models.Gatekeeper;
import me.hikingcarrot7.privee.web.dtos.pagination.PageRequest;

import java.util.List;

@ApplicationScoped
@Transactional
public class GatekeeperRepository {
  @Inject private EntityManager em;

  public List<Gatekeeper> findAll(PageRequest pageRequest) {
    return em
        .createQuery("SELECT g FROM Gatekeeper g WHERE g.isDeleted = false", Gatekeeper.class)
        .setFirstResult((pageRequest.getPage() - 1) * pageRequest.getSize())
        .setMaxResults(pageRequest.getSize())
        .getResultList();
  }

  public Gatekeeper findById(String gatekeeperId) {
    return em
        .createQuery("SELECT g FROM Gatekeeper g WHERE g.id = :gatekeeperId AND g.isDeleted = false", Gatekeeper.class)
        .setParameter("gatekeeperId", gatekeeperId)
        .getSingleResult();
  }

  public Gatekeeper findByEmail(String email) {
    return em
        .createQuery("SELECT g FROM Gatekeeper g WHERE g.email = :email AND g.isDeleted = false", Gatekeeper.class)
        .setParameter("email", email)
        .getSingleResult();
  }

  public Gatekeeper save(Gatekeeper gatekeeper) {
    em.persist(gatekeeper);
    em.flush();
    return gatekeeper;
  }

  public void delete(String id) {
    Gatekeeper gatekeeper = findById(id);
    gatekeeper.setDeleted(true);
    em.merge(gatekeeper);
  }

}
