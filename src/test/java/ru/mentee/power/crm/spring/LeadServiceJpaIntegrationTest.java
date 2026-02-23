package ru.mentee.power.crm.spring;

import org.junit.jupiter.api.Test;import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.spring.model.Lead;import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import ru.mentee.power.crm.spring.repository.DealRepositoryJpa;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;
import ru.mentee.power.crm.spring.service.DealServiceJpa;
import ru.mentee.power.crm.spring.service.LeadServiceJpa;
import java.math.BigDecimal;import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
@Transactional
@ActiveProfiles("test")
class LeadServiceJpaIntegrationTest {

    @Autowired
    private LeadServiceJpa leadService;

    @Autowired
    private DealServiceJpa dealService;

    @Autowired
    private LeadRepositoryJpa leadRepository;

    @Autowired
    private DealRepositoryJpa dealRepository;

    @Test
    void convertLeadToDeal_shouldRollbackOnConstraintViolation() {
        Lead lead = Lead.builder()
                .company("Megacorp")
                .email("iventalll@gmail.com")
                .phone("+7911")
                .status(LeadStatusJpa.NEW)
                .build();

        lead = leadRepository.save(lead);
        UUID leadId = lead.getId();
        LeadStatusJpa originalStatus = lead.getStatus();
        BigDecimal invalidAmount = null;

        Exception exception = assertThrows(Exception.class, () -> {
            dealService.convertLeadToDeal(leadId, invalidAmount);
        });
        Lead updatedLead = leadRepository.findById(leadId).orElse(null);
        assertNotNull(updatedLead);
        assertEquals(originalStatus, updatedLead.getStatus(),
                "Статус лида не должен измениться");
        long dealsCount = dealRepository.count();
        assertEquals(0, dealsCount, "Не должно быть создано ни одной сделки");
    }
}