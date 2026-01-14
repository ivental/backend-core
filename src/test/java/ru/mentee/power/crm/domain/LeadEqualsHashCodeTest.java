package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class LeadEqualsHashCodeTest {
    @Test
    void shouldBeReflexive_whenEqualsCalledOnSameObject() {
        Address adress = new Address("Moscow", "Main St 123", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", adress);
        Lead lead = new Lead(UUID.randomUUID(), contact, "Megacorp", "NEW");
        assertThat(lead).isEqualTo(lead);
    }

    @Test
    void shouldBeSymmetric_whenEqualsCalledOnTwoObjects() {
        UUID uuid = UUID.randomUUID();
        Address adress = new Address("Moscow", "Main St 123", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", adress);
        Lead firstLead = new Lead(uuid, contact, "Megacorp", "NEW");
        Lead secondLead = new Lead(uuid, contact, "Megacorp", "NEW");
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(secondLead).isEqualTo(firstLead);
    }

    @Test
    void shouldBeTransitive_whenEqualsChainOfThreeObjects() {
        UUID uuid = UUID.randomUUID();
        Address adress = new Address("Moscow", "Main St 123", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", adress);
        Lead firstLead = new Lead(uuid, contact, "Megacorp", "NEW");
        Lead secondLead = new Lead(uuid, contact, "Megacorp", "NEW");
        Lead thirdLead = new Lead(uuid, contact, "Megacorp", "NEW");
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(secondLead).isEqualTo(thirdLead);
        assertThat(firstLead).isEqualTo(thirdLead);
    }

    @Test
    void shouldBeConsistent_whenEqualsCalledMultipleTimes() {
        UUID uuid = UUID.randomUUID();
        Address adress = new Address("Moscow", "Main St 123", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", adress);
        Lead firstLead = new Lead(uuid, contact, "Megacorp", "NEW");
        Lead secondLead = new Lead(uuid, contact, "Megacorp", "NEW");
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(firstLead).isEqualTo(secondLead);
    }

    @Test
    void shouldReturnFalse_whenEqualsComparedWithNull() {
        Address adress = new Address("Moscow", "Main St 123", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", adress);
        Lead lead = new Lead(UUID.randomUUID(), contact, "Megacorp", "NEW");
        assertThat(lead).isNotEqualTo(null);
    }

    @Test
    void shouldHaveSameHashCode_whenObjectsAreEqual() {
        UUID uuid = UUID.randomUUID();
        Address adress = new Address("Moscow", "Main St 123", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", adress);
        Lead firstLead = new Lead(uuid, contact, "Megacorp", "NEW");
        Lead secondLead = new Lead(uuid, contact, "Megacorp", "NEW");
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(firstLead.hashCode()).isEqualTo(secondLead.hashCode());
    }

    @Test
    void shouldWorkInHashMap_whenLeadUsedAsKey() {
        UUID uuid = UUID.randomUUID();
        Address adress = new Address("Moscow", "Main St 123", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", adress);
        Lead keyLead = new Lead(uuid, contact, "Megacorp", "NEW");
        Lead lookupLead = new Lead(uuid, contact, "Megacorp", "NEW");
        Map<Lead, String> map = new HashMap<>();
        map.put(keyLead, "CONTACTED");
        String status = map.get(lookupLead);
        assertThat(status).isEqualTo("CONTACTED");
    }

    @Test
    void shouldNotBeEqual_whenIdsAreDifferent() {
        Address adress = new Address("Moscow", "Main St 123", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", adress);
        Lead firstLead = new Lead(UUID.randomUUID(), contact, "Megacorp", "NEW");
        Lead differentLead = new Lead(UUID.randomUUID(), contact, "Megacorp", "NEW");
        assertThat(firstLead).isNotEqualTo(differentLead);
    }
}
