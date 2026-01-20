package ru.mentee.power.crm.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

class InMemoryLeadRepositoryTest {
    private InMemoryLeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLeadRepository();
    }

    @Test
    void findByEmail_shouldReturnEmptyOptional_whenEmailNotFound() {
        Optional<Lead> result = repository.findByEmail("nonexistent@test.com");
        assertThat(result).isEmpty();
    }

    @Test
    void delete_shouldRemoveEmailFromIndex_whenLeadExists() {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, "test@test.com", "+7911", "Company", LeadStatus.NEW);
        repository.save(lead);
        repository.delete(id);
        assertThat(repository.findByEmail("test@test.com")).isEmpty();
    }

    @Test
    void delete_shouldDoNothing_whenLeadDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();
        assertThatNoException().isThrownBy(() ->
                repository.delete(nonExistentId)
        );
    }

    @Test
    void save_shouldOverwriteExistingLead() {
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
