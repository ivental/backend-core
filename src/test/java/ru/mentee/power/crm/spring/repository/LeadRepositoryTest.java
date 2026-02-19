package ru.mentee.power.crm.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class LeadRepositoryTest {

    @Autowired
    private LeadRepositoryJpa repository;

    @Test
    void shouldSaveAndFindLeadById_whenValidData() {
        Lead lead = new Lead(
                null,
                "iv@gmail.com",
                "+7911",
                "Megacorp",
                LeadStatusJpa.NEW,
                null
        );

        Lead saved = repository.save(lead);
        Optional<Lead> found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("iv@gmail.com");
    }

    @Test
    void shouldFindByEmailNative_whenLeadExists() {
        Lead lead = new Lead(
                null,
                "iv@gmail.com",
                "+7911",
                "MEGACORP",
                LeadStatusJpa.NEW,
                null
        );
        repository.save(lead);
        Optional<Lead> found = repository.findByEmailNative("iv@gmail.com");
        assertThat(found).isPresent();
        assertThat(found.get().getCompany()).isEqualTo("MEGACORP");
    }

    @Test
    void shouldReturnEmptyOptional_whenEmailNotFound() {
        Optional<Lead> found = repository.findByEmailNative("nonexistent@test.com");
        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindAllLeads() {
        repository.save(new Lead(null, "iv@gmail.com", "+7911",
                "MEGACORP", LeadStatusJpa.NEW, null));
        repository.save(new Lead(null, "iv1@gmail.com", "+7911",
                "MEGACORP", LeadStatusJpa.NEW, null));
        var all = repository.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    void shouldDeleteLead() {
        Lead lead = repository.save(new Lead(null, "iv@gmail.com", "+7911",
                "MEGACORP", LeadStatusJpa.NEW, null));
        repository.deleteById(lead.getId());
        assertThat(repository.findById(lead.getId())).isEmpty();
    }
}
