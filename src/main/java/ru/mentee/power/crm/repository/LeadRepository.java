package ru.mentee.power.crm.repository;


import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.model.Lead;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeadRepository {
    Lead save(Lead lead);

    Optional<Lead> findById(UUID id);

    Optional<Lead> findByEmail(String email);

    List<Lead> findAll();

    void delete(UUID id);

    int size();
}
