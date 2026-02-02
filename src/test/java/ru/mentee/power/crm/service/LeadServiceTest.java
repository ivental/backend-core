package ru.mentee.power.crm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.infrastructure.InMemoryLeadRepository;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LeadServiceTest {
    private LeadService service;
    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLeadRepository();
        service = new LeadService(repository);
    }

    @Test
    void shouldCreateLead_whenEmailIsUnique() {
        String email = "arasaka@arasaka.com";
        String company = "Arasaka";
        LeadStatus status = LeadStatus.NEW;
        String phone = "+7911";
        Lead result = service.addLead(email, phone,company,status);
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(email);
        assertThat(result.company()).isEqualTo(company);
        assertThat(result.status()).isEqualTo(status);
        assertThat(result.id()).isNotNull();
    }

    @Test
    void shouldThrowException_whenEmailAlreadyExists() {
        String email = "arasaka@arasaka.com";
        service.addLead(email, "+7","arasaka corp", LeadStatus.NEW);
        assertThatThrownBy(() ->
                service.addLead(email, "+7","arasaka inc", LeadStatus.NEW)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email уже существует");
    }

    @Test
    void shouldFindAllLeads() {
        service.addLead("arasaka@arasaka.com", "+7","arasaka", LeadStatus.NEW);
        service.addLead("militech@militech.com", "+7","militech", LeadStatus.CONTACTED);
        List<Lead> result = service.findAll();
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldFindLeadById() {
        Lead created = service.addLead("find@example.com", "+7","Company", LeadStatus.NEW);
        Optional<Lead> result = service.findById(created.id());
        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo("find@example.com");
    }

    @Test
    void shouldFindLeadByEmail() {
        service.addLead("arasaka@arasaka.com", "+7","arasaka", LeadStatus.NEW);
        Optional<Lead> result = service.findByEmail("arasaka@arasaka.com");
        assertThat(result).isPresent();
        assertThat(result.get().company()).isEqualTo("arasaka");
    }

    @Test
    void shouldReturnEmpty_whenLeadNotFound() {
        Optional<Lead> result = service.findByEmail("arasaka@arasaka.com");
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnOnlyNewLeads_whenFindByStatusNew() {
        LeadRepository repository = new InMemoryLeadRepository();
        LeadService leadService = new LeadService(repository);

        for (int i = 1; i <= 3; i++) {
            Lead lead = new Lead(UUID.randomUUID(), "arasaka@arasaka.com", "+7777", "Arasaka",
                    LeadStatus.NEW);
            repository.save(lead);
        }
        for (int i = 4; i <= 8; i++) {
            Lead lead = new Lead(UUID.randomUUID(), "militech@militech.com", "+7111", "Militech",
                    LeadStatus.CONTACTED);
            repository.save(lead);
        }
        for (int i = 9; i <= 10; i++) {
            Lead lead = new Lead(UUID.randomUUID(), "umbrella@umbrella.com", "+7666", "Umbrella",
                    LeadStatus.QUALIFIED);
            repository.save(lead);
        }

        List<Lead> result = leadService.findByStatus(LeadStatus.NEW);
        assertThat(result).hasSize(3);
        assertThat(result).allMatch(lead -> lead.status().equals(LeadStatus.NEW));
    }

    @Test
    void shouldReturnOnlyContactedLeads_whenFindByStatusContacted() {
        LeadRepository repository = new InMemoryLeadRepository();
        LeadService leadService = new LeadService(repository);
        for (int i = 1; i <= 3; i++) {
            Lead lead = new Lead(UUID.randomUUID(), "militech@militech.com", "+7111", "Militech",
                    LeadStatus.NEW);
            repository.save(lead);
        }
        for (int i = 4; i <= 8; i++) {
            Lead lead = new Lead(UUID.randomUUID(), "arasaka@arasaka.com", "+7777", "Arasaka",
                    LeadStatus.CONTACTED);
            repository.save(lead);
        }
        List<Lead> result = leadService.findByStatus(LeadStatus.CONTACTED);
        assertThat(result).hasSize(5);
        assertThat(result).allMatch(lead -> lead.status().equals(LeadStatus.CONTACTED));
    }

    @Test
    void shouldReturnOnlyQualifiedLeads_whenFindByStatusQualified() {
        LeadRepository repository = new InMemoryLeadRepository();
        LeadService leadService = new LeadService(repository);
        for (int i = 9; i <= 10; i++) {
            Lead lead = new Lead(UUID.randomUUID(), "militech@militech.com", "+7111", "Militech",
                    LeadStatus.QUALIFIED);
            repository.save(lead);
        }
        List<Lead> result = leadService.findByStatus(LeadStatus.QUALIFIED);
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(lead -> lead.status().equals(LeadStatus.QUALIFIED));
    }

    @Test
    void shouldReturnEmptyList_whenNoLeadsWithStatus() {
        LeadRepository repository = new InMemoryLeadRepository();
        LeadService leadService = new LeadService(repository);
        for (int i = 1; i <= 5; i++) {
            Lead lead = new Lead(UUID.randomUUID(), "arasaka" + i + "@arasaka.com", "+7911",
                    "Arasaka", i <= 2 ? LeadStatus.NEW : LeadStatus.CONTACTED);
            repository.save(lead);
        }
        List<Lead> result = leadService.findByStatus(LeadStatus.QUALIFIED);
        assertThat(result).isEmpty();
        assertThat(result).hasSize(0);
    }
}
