package ru.mentee.power.crm.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.spring.model.Deal;
import java.util.UUID;
@Repository
public interface DealRepositoryJpa extends JpaRepository<Deal, UUID> {

}
