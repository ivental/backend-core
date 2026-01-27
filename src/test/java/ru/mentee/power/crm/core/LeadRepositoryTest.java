package ru.mentee.power.crm.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LeadRepositoryTest {

    @Test
    @DisplayName("Should automatically deduplicate leads by id")
    void shouldDeduplicateLeadsById() {
        LeadRepository leadRepository = new LeadRepository();
        Address address = new Address("Moscow", "Main 123 Street", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "MegaCorp", "NEW");
        boolean firstAdd = leadRepository.add(lead);
        boolean secondAdd = leadRepository.add(lead);
        assertThat(leadRepository.size()).isEqualTo(1);
        assertThat(secondAdd).isFalse();
    }

    @Test
    @DisplayName("Should allow different leads with different ids")
    void shouldAllowDifferentLeads() {
        LeadRepository leadRepository = new LeadRepository();
        Address address = new Address("Moscow", "Main 123 Street", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", address);
        Lead lead1 = new Lead(UUID.randomUUID(), contact, "Megacorp", "NEW");
        Lead lead2 = new Lead(UUID.randomUUID(), contact, "Megacorp", "NEW");
        boolean firstAdd = leadRepository.add(lead1);
        boolean secondAdd = leadRepository.add(lead2);
        assertThat(leadRepository.size()).isEqualTo(2);
        assertThat(firstAdd).isTrue();
        assertThat(secondAdd).isTrue();
    }

    @Test
    @DisplayName("Should find existing lead through contains")
    void shouldFindExistingLead() {
        LeadRepository leadRepository = new LeadRepository();
        Address address = new Address("Moscow", "Main 123 Street", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "Megacorp", "NEW");
        leadRepository.add(lead);
        boolean contains = leadRepository.contains(lead);
        assertThat(contains).isTrue();
    }

    @Test
    @DisplayName("Should return unmodifiable set from findAll")
    void shouldReturnUnmodifiableSet() {
        LeadRepository leadRepository = new LeadRepository();
        Address address = new Address("Moscow", "Main 123 Street", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "Megacorp", "NEW");
        leadRepository.add(lead);
        Set<Lead> result = leadRepository.findAll();
        assertThatThrownBy(() -> {
            result.add(new Lead(UUID.randomUUID(), contact, "OtherCorp", "NEW"));
        }).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Should perform contains() faster than ArrayList")
    void shouldPerformFasterThanArrayList() {
        LeadRepository leadRepository = new LeadRepository();
        List<Lead> arrayList = new ArrayList<>();
        Address address = new Address("Moscow", "Main 123 Street", "10001");
        Contact contact = new Contact("iventalll@gmail.com", "+7911", address);
        List<Lead> allLeads = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Lead lead = new Lead(UUID.randomUUID(), contact, "Company" + i, "NEW");
            allLeads.add(lead);
            leadRepository.add(lead);
            arrayList.add(lead);
        }
        Lead targetLead = allLeads.get(5000);
        long hashSetStart = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            leadRepository.contains(targetLead);
        }
        long hashSetTime = System.nanoTime() - hashSetStart;
        long arrayListStart = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            arrayList.contains(targetLead);
        }
        long arrayListTime = System.nanoTime() - arrayListStart;
        assertThat(arrayListTime).isGreaterThan(hashSetTime * 10);
    }

    @Test
    void shouldBeEmpty_whenCreated() {
        LeadRepository repo = new LeadRepository();
        assertThat(repo.size()).isZero();
        assertThat(repo.findAll()).isEmpty();
    }
}


