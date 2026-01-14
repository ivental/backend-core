package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ContactTest {
    @Test
    void shouldCreateContact_whenValidData() {
        Address address = new Address("San Francisco", "123 Main St", "94105");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", address);
        assertThat(contact.address()).isEqualTo(address);
        assertThat(contact.address().city()).isEqualTo("San Francisco");
        assertThat(contact.email()).isEqualTo("iventalll@gmail.com");
        assertThat(contact.phone()).isEqualTo("+7911");
    }

    @Test
    void shouldDelegateToAddress_whenAccessingCity() {
        Address address = new Address("San Francisco", "123 Main St", "94105");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", address);
        assertThat(contact.address().city()).isEqualTo("San Francisco");
        assertThat(contact.address().street()).isEqualTo("123 Main St");

    }

    @Test
    void shouldThrowException_whenAddressIsNull() {
        assertThatThrownBy(() ->
                new Contact("iventalll@gmail.com", "+7911", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Адрес не может быть пустым");
    }
}

