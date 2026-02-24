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
        Lead lead = Lead.builder()
                .company("Megacorp")
                .email("iventalio@gmail.com")
                .phone("+7911")
                .status(LeadStatusJpa.NEW)
                .build();


        Lead saved = repository.save(lead);
        Optional<Lead> found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("iventalio@gmail.com");
    }

    @Test
    void shouldFindByEmailNative_whenLeadExists() {
        Lead lead = Lead.builder()
                .company("Megacorp")
                .email("ivi@gmail.com")
                .phone("+7911")
                .status(LeadStatusJpa.NEW)
                .build();
        repository.save(lead);
        Optional<Lead> found = repository.findByEmail("ivi@gmail.com");
        assertThat(found).isPresent();
        assertThat(found.get().getCompany()).isEqualTo("Megacorp");
    }

    @Test
    void shouldReturnEmptyOptional_whenEmailNotFound() {
        Optional<Lead> found = repository.findByEmail("nonexistent@test.com");
        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindAllLeads() {
        Lead lead = Lead.builder()
                .company("Megacorp")
                .email("iventalll@gmail.com")
                .phone("+7911")
                .status(LeadStatusJpa.NEW)
                .build();
        Lead leadFirst = Lead.builder()
                .company("MegaCorp")
                .email("iventalll1@gmail.com")
                .phone("+7911")
                .status(LeadStatusJpa.NEW)
                .build();
        repository.save(lead);
        repository.save(leadFirst);
        var all = repository.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    void shouldDeleteLead() {
        Lead lead = Lead.builder()
                .company("Megacorp")
                .email("iventalll@gmail.com")
                .phone("+7911")
                .status(LeadStatusJpa.NEW)
                .build();
        repository.save(lead);
        repository.deleteById(lead.getId());
        assertThat(repository.findById(lead.getId())).isEmpty();
    }
}
