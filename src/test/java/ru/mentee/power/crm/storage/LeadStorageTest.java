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

    @Test
    void shouldThrowException_whenStorageIsFull() {
        LeadStorage storage = new LeadStorage();
        Address address = new Address("Moscow", "Main St 123", "10001");
        for (int i = 0; i < 100; i++) {
            Contact contact = new Contact("email" + i + "@test.com", "+7911", address);
            Lead lead = new Lead(UUID.randomUUID(), contact, "Company" + i, "NEW");
            storage.add(lead);
        }
        Contact contact101 = new Contact("email101@test.com", "+7911", address);
        Lead lead101 = new Lead(UUID.randomUUID(), contact101, "Company101", "NEW");
        assertThatThrownBy(() -> storage.add(lead101))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Storage is full, cannot add more leads");
    }

    @Test
    void shouldNotFindDuplicate_whenEmailIsDifferent() {
        LeadStorage storage = new LeadStorage();
        Address address = new Address("Moscow", "Main St 123", "10001");
        Contact contact1 = new Contact("email1@test.com", "+7911", address);
        Contact contact2 = new Contact("email2@test.com", "+7911", address);
        Lead lead1 = new Lead(UUID.randomUUID(), contact1, "Company1", "NEW");
        Lead lead2 = new Lead(UUID.randomUUID(), contact2, "Company2", "NEW");
        storage.add(lead1);
        boolean added = storage.add(lead2);
        assertThat(added).isTrue();
        assertThat(storage.size()).isEqualTo(2);
    }

    @Test
    void findAll_shouldReturnEmptyArray_whenStorageIsEmpty() {
        LeadStorage storage = new LeadStorage();
        Lead[] result = storage.findAll();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        assertThat(result).hasSize(0);
    }

    @Test
    void size_shouldReturnZero_whenStorageIsEmpty() {
        LeadStorage storage = new LeadStorage();
        assertThat(storage.size()).isZero();
    }

    @Test
    void shouldHandleNullCellsCorrectly() {
        LeadStorage storage = new LeadStorage();
        Address address = new Address("Moscow", "Main St 123", "10001");
        Contact contact = new Contact("test@test.com", "+7911", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "Company", "NEW");
        storage.add(lead);
        Lead[] result = storage.findAll();
        assertThat(result).hasSize(1);
        assertThat(result[0]).isEqualTo(lead);
    }
}