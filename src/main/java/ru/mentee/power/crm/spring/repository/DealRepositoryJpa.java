package ru.mentee.power.crm.spring.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.spring.model.Deal;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DealRepositoryJpa extends JpaRepository<Deal, UUID> {

    @EntityGraph(attributePaths = {"dealProducts", "dealProducts.product"})
    @Query("SELECT d FROM Deal d WHERE d.id = :id")
    Optional<Deal> findDealWithProducts(@Param("id") UUID id);
}
