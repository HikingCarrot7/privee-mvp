package me.hikingcarrot7.privee.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import me.hikingcarrot7.privee.models.Resident;
import me.hikingcarrot7.privee.web.dtos.pagination.PageRequest;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class ResidentRepository {
  @Inject private EntityManager em;

  public List<Resident> findAll(PageRequest pageRequest) {
    return em
        .createQuery("SELECT r FROM Resident r WHERE r.isDeleted = false", Resident.class)
        .setFirstResult((pageRequest.getPage() - 1) * pageRequest.getSize())
        .setMaxResults(pageRequest.getSize())
        .getResultList();
  }

  public Optional<Resident> findById(String residentId) {
    Resident residentMaybe = em
        .createQuery("SELECT r FROM Resident r WHERE r.id = :residentId AND r.isDeleted = false", Resident.class)
        .setParameter("residentId", residentId)
        .getSingleResult();
    return Optional.ofNullable(residentMaybe);
  }

  public Optional<Resident> findByEmail(String email) {
    Resident residentMaybe = em
        .createQuery("SELECT r FROM Resident r WHERE r.email = :email AND r.isDeleted = false", Resident.class)
        .setParameter("email", email)
        .getSingleResult();
    return Optional.ofNullable(residentMaybe);
  }

  public Resident save(Resident resident) {
    em.persist(resident);
    em.flush();
    return resident;
  }

  public void delete(String residentId) {
    Optional<Resident> residentMaybe = findById(residentId);
    if (residentMaybe.isPresent()) {
      Resident resident = residentMaybe.get();
      resident.setDeleted(true);
      em.merge(resident);
    }
  }

}
