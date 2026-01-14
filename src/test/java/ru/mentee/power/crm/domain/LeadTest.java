package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LeadTest {

    @Test
    void shouldCreateLead_whenValidData() {
        Address address = new Address("Moscow", "123 Main st", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "Megacorp", "NEW");
        assertThat(lead.contact()).isEqualTo(contact);
    }

    @Test
    void shouldAccessEmailThroughDelegation_whenLeadCreated() {
        Address address = new Address("Moscow", "123 Main st", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "Megacorp", "NEW");
        assertThat(lead.contact().email()).isEqualTo("iventalll@gmail.com");
        assertThat(lead.contact().address().city()).isEqualTo("Moscow");
    }

    @Test
    void shouldBeEqual_whenSameIdButDifferentContact() {
        Address address = new Address("Moscow", "123 Main st", "10001");
        Contact contact1 = new Contact("iventalll@gmail.com", "+7911", address);
        Contact contact2 = new Contact("iventalll@gmail.com", "+7912", address);
        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614177777");
        Lead lead1 = new Lead(uuid, contact1, "Megacorp", "NEW");
        Lead lead2 = new Lead(uuid, contact2, "Megacorp", "NEW");
        assertThat(lead1).isNotEqualTo(lead2);
    }

    @Test
    void shouldThrowException_whenContactIsNull() {
        assertThatThrownBy(() ->
                new Lead(UUID.randomUUID(), null, "Megacorp", "NEW"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Контакт не может быть пустым");
    }

        @Test
        void shouldThrowException_whenInvalidStatus() {
            Address address = new Address("Moscow", "123 Main st", "10001");
            Contact contact = new Contact("iventalll@gmail.com", "+7911", address);
            assertThatThrownBy(() ->
                    new Lead(UUID.randomUUID(), contact, "Megacorp", "НОВЫЙ"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("только \"NEW\", \"QUALIFIED\", \"CONVERTED\" разрешены");
        }

    @Test
    void shouldDemonstrateThreeLevelComposition_whenAccessingCity() {
        Address address = new Address("Moscow", "123 Main st", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", address);
        Lead lead = new Lead(UUID.randomUUID(),contact,"Megacorp","NEW");
        assertThat(lead.contact().address().city()).isEqualTo("Moscow");
    }
}

