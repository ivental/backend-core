package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AddressTest {
    @Test
    void shouldCreateAddress_whenValidData() {
        Address address = new Address("San Francisco", "123 Main St", "94105");
        assertThat(address.city()).isEqualTo("San Francisco");
        assertThat(address.street()).isEqualTo("123 Main St");
        assertThat(address.zip()).isEqualTo("94105");
    }

    @Test
    void shouldBeEqual_whenSameData() {
        Address address1 = new Address("San Francisco", "123 Main St", "94105");
        Address address2 = new Address("San Francisco", "123 Main St", "94105");
        assertThat(address1).isEqualTo(address2);
        assertThat(address2).isEqualTo(address1);
        assertThat(address1.hashCode()).isEqualTo(address2.hashCode());
    }

    @Test
    void shouldThrowException_whenCityIsNull() {
        assertThatThrownBy(() ->
                new Address(null, "123 Main St", "94105")).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Город не может быть пустым");
    }

    @Test
    void shouldThrowException_whenZipIsBlank() {
        assertThatThrownBy(() ->
                new Address("San Francisco", "123 Main St", "")).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Zip code не может быть пустым");
    }

    @Test
    void shouldAllowEmptyOrNullStreet() {
        Address address1 = new Address("Moscow", null, "123456");
        Address address2 = new Address("Moscow", "", "123456");

        assertThat(address1.street()).isNull();
        assertThat(address2.street()).isEmpty();
    }

    @Test
    void shouldBeImmutable() {
        String city = "Moscow";
        Address address = new Address(city, "Street", "123456");
        city = "Changed";
        assertThat(address.city()).isEqualTo("Moscow");
    }

    @Test
    void shouldContainAllFieldsInToString() {
        Address address = new Address("Moscow", "Tverskaya", "123456");
        String str = address.toString();
        assertThat(str).contains("Moscow");
        assertThat(str).contains("Tverskaya");
        assertThat(str).contains("123456");
    }

    @Test
    void shouldNotBeEqual_whenDifferentData() {
        Address address1 = new Address("Moscow", "Street1", "123456");
        Address address2 = new Address("Moscow", "Street2", "123456");
        Address address3 = new Address("SPb", "Street1", "123456");
        Address address4 = new Address("Moscow", "Street1", "654321");
        assertThat(address1).isNotEqualTo(address2);
        assertThat(address1).isNotEqualTo(address3);
        assertThat(address1).isNotEqualTo(address4);
    }
}

