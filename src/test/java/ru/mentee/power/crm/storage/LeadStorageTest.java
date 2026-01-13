package ru.mentee.power.crm.storage;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Lead;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class LeadStorageTest {

    @Test
    void shouldAddLead_whenLeadIsUnique() {
        LeadStorage storage = new LeadStorage();
        Lead uniqueLead = new Lead(UUID.randomUUID(), "ilia@gmail.com", "+7911", "MegaCorp", "NEW");
        boolean added = storage.add(uniqueLead);
        assertThat(added).isTrue();
        assertThat(storage.size()).isEqualTo(1);
        assertThat(storage.findAll()).containsExactly(uniqueLead);
    }

    @Test
    void shouldRejectDuplicate_whenEmailAlreadyExists() {
        LeadStorage storage = new LeadStorage();
        Lead existingLead = new Lead(UUID.randomUUID(), "ilia@gmail.com", "+7911", "MegaCorp", "NEW");
        Lead duplicateLead = new Lead(UUID.randomUUID(), "ilia@gmail.com", "+7912",
                "SuperCorp", "NEW");
        storage.add(existingLead);
        boolean added = storage.add(duplicateLead);
        assertThat(added).isFalse();
        assertThat(storage.size()).isEqualTo(1);
        assertThat(storage.findAll()).containsExactly(existingLead);
    }

    @Test
    void shouldThrowException_whenStorageIsFull() {
        LeadStorage storage = new LeadStorage();
        for (int index = 0; index < 100; index++) {
            storage.add(new Lead(UUID.randomUUID(), "lead" + index + "@mail.ru",
                    "+7000", "Company", "NEW"));
        }
        Lead hundredFirstLead = new Lead(UUID.randomUUID(), "lead101@mail.ru", "+7001", "Company", "NEW");
        assertThatThrownBy(() -> storage.add(hundredFirstLead))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Storage is full");
    }

    @Test
    void shouldReturnOnlyAddedLeads_whenFindAllCalled() {
        LeadStorage storage = new LeadStorage();
        Lead firstLead = new Lead(UUID.randomUUID(), "ilia@gmail.com", "+7911", "MegaCorp", "NEW");
        Lead secondLead = new Lead(UUID.randomUUID(), "ilyusha@mail.ru", "+7912",
                "SuperCorp", "NEW");
        storage.add(firstLead);
        storage.add(secondLead);
        Lead[] result = storage.findAll();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(firstLead, secondLead);
    }
}