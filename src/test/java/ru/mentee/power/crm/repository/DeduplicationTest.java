package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.infrastructure.InMemoryLeadRepository;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

public class DeduplicationTest {
    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLeadRepository() {
        };
    }

    @Test
    void shouldSaveBothLeads_evenWithSameEmailAndPhone_becauseRepositoryDoesNotCheckBusinessRules() {
        String sameEmail = "iventalll@mail.ru";
        String samePhone = "+7911";
        Lead lead1 = new Lead(UUID.randomUUID(), sameEmail, samePhone, "Megacorp", LeadStatus.NEW);
        Lead lead2 = new Lead(UUID.randomUUID(), sameEmail, samePhone, "Supacorp", LeadStatus.CONTACTED);
        repository.save(lead1);
        repository.save(lead2);
        assertThat(repository.size()).isEqualTo(2);
    }

    @Test
    void delete_shouldDoNothing_whenLeadDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();
        assertThatNoException().isThrownBy(() ->
                repository.delete(nonExistentId)
        );
        assertThat(repository.size()).isEqualTo(0);
    }

    @Test
    void findByEmail_shouldReturnEmptyOptional_whenEmailNotFound() {
        Optional<Lead> result = repository.findByEmail("nonexistent@test.com");

        assertThat(result).isEmpty();
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenIdNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        Optional<Lead> result = repository.findById(nonExistentId);

        assertThat(result).isEmpty();
    }

    @Test
    void save_shouldOverwriteExistingLead_whenSameId() {
        UUID sameId = UUID.randomUUID();
        Lead lead1 = new Lead(sameId, "email1@test.com", "+7911", "Company1", LeadStatus.NEW);
        Lead lead2 = new Lead(sameId, "email2@test.com", "+7922", "Company2", LeadStatus.CONTACTED);

        repository.save(lead1);
        repository.save(lead2);

        Optional<Lead> found = repository.findById(sameId);
        assertThat(found).isPresent();
        assertThat(found.get().email()).isEqualTo("email2@test.com");
        assertThat(repository.size()).isEqualTo(1);
    }
}
