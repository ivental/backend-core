package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.infrastructure.InMemoryLeadRepository;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(repository.size()).isEqualTo(2);  // Оба сохранились
    }
}
