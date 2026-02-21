package ru.mentee.power.crm.spring.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")

public class LeadRepositoryJpaTest {
    @Autowired
    private LeadRepositoryJpa repository;

    private Lead lead1;
    private Lead lead2;

    @BeforeEach
    void setUpForDerivedTests() {
        lead1 = Lead.builder()
                .email("iv@gmail.com")
                .phone("+7911")
                .company("Arasaka")
                .status(LeadStatusJpa.NEW)
                .createdAt(OffsetDateTime.now().minusDays(5))
                .build();
        repository.save(lead1);

        lead2 = Lead.builder()
                .email("iventalll@gmail.com")
                .phone("+7911")
                .company("Militech")
                .status(LeadStatusJpa.CONTACTED)
                .createdAt(OffsetDateTime.now().minusDays(3))
                .build();
        repository.save(lead2);
    }

    @Test
    void findByEmail_shouldReturnLead_whenExists() {
        Optional<Lead> found = repository.findByEmail("iv@gmail.com");
        assertThat(found).isPresent();
        assertThat(found.get().getCompany()).isEqualTo("Arasaka");
    }

    @Test
    void findByStatus_shouldReturnFilteredLeads() {
        List<Lead> newLeads = repository.findByStatus(LeadStatusJpa.NEW);
        assertThat(newLeads).hasSize(1);
        assertThat(newLeads.getFirst().getEmail()).isEqualTo("iv@gmail.com");
    }

    @Test
    void findByStatusIn_shouldReturnLeadsWithMultipleStatuses() {
        List<LeadStatusJpa> statuses = List.of(LeadStatusJpa.NEW, LeadStatusJpa.CONTACTED);
        List<Lead> found = repository.findByStatusIn(statuses);
        assertThat(found).hasSize(2);
    }

    @Test
    void findAll_withPageable_shouldReturnPage() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<Lead> page = repository.findAll(pageRequest);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getNumber()).isZero();
    }

    @Test
    void countByStatus_shouldReturnCorrectCount() {
        long count = repository.countByStatus(LeadStatusJpa.NEW);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenEmailExists() {
        boolean exists = repository.existsByEmail("iv@gmail.com");
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenEmailNotFound() {
        boolean exists = repository.existsByEmail("nonexistent@test.com");
        assertThat(exists).isFalse();
    }

    @Test
    void findByStatusAndCompany_shouldReturnMatchingLeads() {
        List<Lead> leads = repository.findByStatusAndCompany(LeadStatusJpa.NEW, "Arasaka");
        assertThat(leads).hasSize(1);
        assertThat(leads.getFirst().getEmail()).isEqualTo("iv@gmail.com");
    }

    @Test
    void findByStatusOrderByCreatedAtDesc_shouldReturnSortedLeads() {
        List<Lead> leads = repository.findByStatusOrderByCreatedAtDesc(LeadStatusJpa.CONTACTED);
        assertThat(leads).hasSize(1);
        assertThat(leads.getFirst().getEmail()).isEqualTo("iventalll@gmail.com");
    }

    @Test
    void findCreatedAfter_shouldReturnRecentLeads() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime threeDaysAgo = now.minusDays(3).withNano(0);
        List<Lead> leads = repository.findCreatedAfter(threeDaysAgo);
        assertThat(leads).hasSize(1);
        assertThat(leads.getFirst().getEmail()).isEqualTo("iventalll@gmail.com");
    }

    @Test
    void findByCompanyOrderedByDate_shouldReturnSortedLeads() {
        List<Lead> leads = repository.findByCompanyOrderedByDate("Arasaka");
        assertThat(leads).hasSize(1);
        assertThat(leads.getFirst().getEmail()).isEqualTo("iv@gmail.com");
    }
}
