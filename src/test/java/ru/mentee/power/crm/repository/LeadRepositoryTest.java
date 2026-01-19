package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.infrastructure.InMemoryLeadRepository;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class LeadRepositoryTest {
    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLeadRepository();
    }

    @Test
    void shouldSaveAndFindLeadById_whenLeadSaved() {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, "iventalll@gmail.com", "+7911", "Megacorp", LeadStatus.NEW);
        repository.save(lead);
        assertThat(repository.findById(id)).isNotNull();
    }

    @Test
    void shouldReturnNull_whenLeadNotFound() {
        UUID uuid = UUID.randomUUID();
        Optional<Lead> result = repository.findById(uuid);
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnAllLeads_whenMultipleLeadsSaved() {
        Lead lead1 = new Lead(UUID.randomUUID(), "iventalll@gmail.com", "+7911", "Megacorp",
                LeadStatus.NEW);
        Lead lead2 = new Lead(UUID.randomUUID(), "iventalll@gmail.com", "+7911", "Megacorp",
                LeadStatus.NEW);
        Lead lead3 = new Lead(UUID.randomUUID(), "iventalll@gmail.com", "+7911", "Megacorp",
                LeadStatus.NEW);
        repository.save(lead1);
        repository.save(lead2);
        repository.save(lead3);
        List<Lead> result = repository.findAll();
        assertThat(result).hasSize(3);
    }

    @Test
    void shouldDeleteLead_whenLeadExists() {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, "iventalll@gmail.com", "+7911", "Megacorp",
                LeadStatus.NEW);
        repository.save(lead);
        repository.delete(id);
        assertThat(repository.findById(id)).isEmpty();
        assertThat(repository.size()).isEqualTo(0);
    }

    @Test
    void shouldOverwriteLead_whenSaveWithSameId() {
        UUID id = UUID.randomUUID();
        Lead lead1 = new Lead(id, "iventalll@gmail.com", "+7911", "Megacorp",
                LeadStatus.NEW);
        Lead lead2 = new Lead(id, "ivental@gmail.com", "+7911", "Megacorp",
                LeadStatus.NEW);
        repository.save(lead1);
        repository.save(lead2);
        assertThat(repository.findById(id)).contains(lead2);
        assertThat(repository.size()).isEqualTo(1);
    }
}

