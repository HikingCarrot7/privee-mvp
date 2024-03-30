package me.hikingcarrot7.privee.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import me.hikingcarrot7.privee.models.Admin;
import me.hikingcarrot7.privee.web.dtos.pagination.PageRequest;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class AdminRepository {
  @Inject private EntityManager em;

  public List<Admin> findAll(PageRequest pageRequest) {
    return em
        .createQuery("SELECT a FROM Admin a WHERE a.isDeleted = false", Admin.class)
        .setFirstResult((pageRequest.getPage() - 1) * pageRequest.getSize())
        .setMaxResults(pageRequest.getSize())
        .getResultList();
  }

  public Optional<Admin> findById(String id) {
    Admin adminMaybe = em
        .createQuery("SELECT a FROM Admin a WHERE a.id = :id AND a.isDeleted = false", Admin.class)
        .setParameter("id", id)
        .getSingleResult();
    return Optional.ofNullable(adminMaybe);
  }

  public Optional<Admin> findByEmail(String email) {
    Admin adminMaybe = em
        .createQuery("SELECT a FROM Admin a WHERE a.email = :email AND a.isDeleted = false", Admin.class)
        .setParameter("email", email)
        .getSingleResult();
    return Optional.ofNullable(adminMaybe);
  }

  public Admin save(Admin admin) {
    em.persist(admin);
    em.flush();
    return admin;
  }

  public void delete(String id) {
    Optional<Admin> adminMaybe = findById(id);
    if (adminMaybe.isPresent()) {
      Admin admin = adminMaybe.get();
      admin.setDeleted(true);
      em.merge(admin);
    }
  }

}
