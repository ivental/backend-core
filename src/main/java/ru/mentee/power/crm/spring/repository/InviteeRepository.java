package ru.mentee.power.crm.spring.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.mentee.power.crm.spring.model.Invitee;

public interface InviteeRepository extends JpaRepository<Invitee, UUID> {
  boolean existsByEmail(@NotBlank @Email String email);
}
