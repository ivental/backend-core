package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomerTest {
    @Test
    void shouldReuseContact_whenCreatingCustomer() {
        Address address = new Address("Moscow", "Main st 123", "10001");
        Address addressBilling = new Address("Moscow", "Moscow St 777", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", address);
        Customer customer = new Customer(UUID.randomUUID(),contact,addressBilling,"SILVER");
        assertThat(customer.contact().address()).isNotEqualTo(customer.billingAddress());
        assertThat(customer.contact().address().street()).isEqualTo("Main st 123");
        assertThat(customer.billingAddress().street()).isEqualTo("Moscow St 777");
    }

    @Test
    void shouldDemonstrateContactReuse_acrossLeadAndCustomer() {
        Address address = new Address("Moscow", "Main st 123", "10001");
        Address addressBilling = new Address("Moscow", "Moscow St 777", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", address);
        Customer customer = new Customer(UUID.randomUUID(),contact,addressBilling,"SILVER");
        Lead lead = new Lead(UUID.randomUUID(),contact,"Megacorp","NEW");
        assertThat(customer.contact()).isEqualTo(lead.contact());
        assertThat(customer.contact()).isSameAs(lead.contact());
        assertThat(customer.contact().email()).isEqualTo("iventalll@gmail.com");
        assertThat(lead.contact().email()).isEqualTo("iventalll@gmail.com");
    }
}

