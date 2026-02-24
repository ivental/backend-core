package ru.mentee.power.crm.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class LeadServiceJpaTest {

    @Autowired
    private LeadServiceJpa service;

    @Autowired
    private LeadRepositoryJpa repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        for (int i = 1; i <= 3; i++) {
            Lead lead = Lead.builder()
                    .email("lead" + i + "@example.com")
                    .phone(i + "123")
                    .company("Company " + i)
                    .status(LeadStatusJpa.NEW)
                    .build();
            repository.save(lead);
        }

        for (int i = 1; i <= 3; i++) {
            Lead lead = Lead.builder()
                    .email("lost" + i + "@example.com")
                    .phone(i + "456")
                    .company("LostCompany " + i)
                    .status(LeadStatusJpa.CONTACTED)
                    .build();
            repository.save(lead);
        }
    }

    @Test
    void convertNewToContacted_shouldUpdateMultipleLeads() {
        int updated = service.convertNewToContacted();
        assertThat(updated).isEqualTo(3);
        long newCount = repository.countByStatus(LeadStatusJpa.NEW);
        assertThat(newCount).isEqualTo(0);
        long contactedCount = repository.countByStatus(LeadStatusJpa.CONTACTED);
        assertThat(contactedCount).isEqualTo(6);
    }

    @Test
    void archiveOldLeads() {

        int deleted = service.archiveOldLeads(LeadStatusJpa.CONTACTED);
        assertThat(deleted).isEqualTo(3);
        long contactedCount = repository.countByStatus(LeadStatusJpa.CONTACTED);
        assertThat(contactedCount).isEqualTo(0);
        long newCount = repository.countByStatus(LeadStatusJpa.NEW);
        assertThat(newCount).isEqualTo(3);
    }
}


