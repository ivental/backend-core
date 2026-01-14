package ru.mentee.power.crm.storage;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class LeadStorageTest {

    @Test
    void shouldAddLead_whenLeadIsUnique() {
        Address adress = new Address("Moscow", "Main St 123", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", adress);
        Lead uniqueLead = new Lead(UUID.randomUUID(), contact, "Megacorp", "NEW");
        LeadStorage storage = new LeadStorage();
        boolean added = storage.add(uniqueLead);
        assertThat(added).isTrue();
        assertThat(storage.size()).isEqualTo(1);
        assertThat(storage.findAll()).containsExactly(uniqueLead);
    }

    @Test
    void shouldRejectDuplicate_whenEmailAlreadyExists() {
        LeadStorage storage = new LeadStorage();
        Address adress = new Address("Moscow", "Main St 123", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", adress);
        Lead existingLead = new Lead(UUID.randomUUID(), contact, "Megacorp", "NEW");
        Lead duplicateLead = new Lead(UUID.randomUUID(), contact, "Megacorp", "NEW");
        storage.add(existingLead);
        boolean added = storage.add(duplicateLead);
        assertThat(added).isFalse();
        assertThat(storage.size()).isEqualTo(1);
        assertThat(storage.findAll()).containsExactly(existingLead);
    }
}