package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactTest {
    @Test
    void shouldCreateContact_whenValidData() {
        Contact contact = new Contact("John", "Doe", "john@example.com");
        assertThat(contact.firstName()).isEqualTo("John");
        assertThat(contact.lastName()).isEqualTo("Doe");
        assertThat(contact.email()).isEqualTo("john@example.com");
    }

    @Test
    void shouldBeEqual_whenSameData() {
        Contact contact1 = new Contact("John", "Doe", "john@example.com");
        Contact contact2 = new Contact("John", "Doe", "john@example.com");
        assertThat(contact1.equals(contact2)).isTrue();
        assertThat(contact2.equals(contact1)).isTrue(); // проверяем симметричность
        assertThat(contact1.hashCode()).isEqualTo(contact2.hashCode());
    }

    @Test
    void shouldNotBeEqual_whenDifferentData() {
        Contact contact1 = new Contact("John", "Doe", "john@example.com");
        Contact contact2 = new Contact("Dohn", "Joe", "joe@example.com");
        assertThat(contact1.equals(contact2)).isFalse();
    }
}
