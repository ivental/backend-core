package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class LeadRepositoryTest {
    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new LeadRepository();
    }

    @Test
    void shouldSaveAndFindLeadById_whenLeadSaved() {
        Lead lead = new Lead("1", "iventalll@gmail.com", "+7911", "Megacorp", "NEW");
        repository.save(lead);
        assertThat(repository.findById("1")).isNotNull();
    }

    @Test
    void shouldReturnNull_whenLeadNotFound() {
        Lead result = repository.findById("unknown-id");
        assertThat(result).isNull();
    }

    @Test
    void shouldReturnAllLeads_whenMultipleLeadsSaved() {
        Lead lead1 = new Lead("1", "iventalll@gmail.com", "+7911", "Megacorp", "NEW");
        Lead lead2 = new Lead("2", "iventalll@gmail.com", "+7911", "Megacorp", "NEW");
        Lead lead3 = new Lead("3", "iventalll@gmail.com", "+7911", "Megacorp", "NEW");
        repository.save(lead1);
        repository.save(lead2);
        repository.save(lead3);
        List<Lead> result = repository.findAll();
        assertThat(result).hasSize(3);
    }

    @Test
    void shouldDeleteLead_whenLeadExists() {
        Lead lead = new Lead("1", "iventalll@gmail.com", "+7911", "Megacorp", "NEW");
        repository.save(lead);
        repository.delete("1");
        assertThat(repository.findById("1")).isNull();
        assertThat(repository.size()).isEqualTo(0);
    }

    @Test
    void shouldOverwriteLead_whenSaveWithSameId() {
        Lead lead1 = new Lead("lead-1", "iventalll@gmail.com", "+7911", "Megacorp", "NEW");
        Lead lead2 = new Lead("lead-1", "ivental@gmail.com", "+7911", "Megacorp", "NEW");
        repository.save(lead1);
        repository.save(lead2);
        assertThat(repository.findById("lead-1")).isEqualTo(lead2);
        assertThat(repository.size()).isEqualTo(1);
    }
}

