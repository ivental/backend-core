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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class PropagationIntegrationTest {
    @Autowired
    private PropagationDemoService demoService;

    @Autowired
    private LeadRepositoryJpa leadRepository;

    @BeforeEach
    void setUp() {
        leadRepository.deleteAll();
    }

    @Test
    void propagation_REQUIRED_shouldReuseTransaction() {
        Lead lead = createLead("test1@example.com");
        demoService.methodWithRequired(lead.getId());
        assertLeadStatus(lead.getId(), LeadStatusJpa.CONTACTED);
    }

    @Test
    void propagation_REQUIRES_NEW_shouldCreateNewTransaction() {
        Lead lead = createLead("test2@example.com");
        demoService.methodWithRequiresNew(lead.getId());
        assertLeadStatus(lead.getId(), LeadStatusJpa.QUALIFIED);
    }

    @Test
    @Transactional
    void propagation_MANDATORY_worksInExistingTransaction() {
        Lead lead = createLead("test3@example.com");
        demoService.methodWithMandatory(lead.getId());
        assertLeadStatus(lead.getId(), LeadStatusJpa.LOST);
    }

    private Lead createLead(String email) {
        Lead lead = Lead.builder()
                .company("Megacorp")
                .email("iventalll@gmail.com")
                .phone("+7911")
                .status(LeadStatusJpa.NEW)
                .build();
        return leadRepository.save(lead);
    }

    private void assertLeadStatus(UUID id, LeadStatusJpa expected) {
        Lead lead = leadRepository.findById(id).orElseThrow();
        assertThat(lead.getStatus()).isEqualTo(expected);
    }
}
