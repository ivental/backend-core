package ru.mentee.power.crm.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.spring.model.Lead;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeadRepositoryJpa extends JpaRepository<Lead, UUID> {
    @Query(value = "SELECT * FROM leads WHERE email = ?1", nativeQuery = true)
    Optional<Lead> findByEmailNative(String email);

    @Query(value = "SELECT * FROM leads WHERE status = ?1", nativeQuery = true)
    java.util.List<Lead> findByStatusNative(String status);

    Optional<Lead> findByEmail(String email);
}
