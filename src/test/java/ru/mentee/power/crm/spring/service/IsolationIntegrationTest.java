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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class IsolationIntegrationTest {

    @Autowired
    private IsolationDemoService demoService;

    @Autowired
    private LeadRepositoryJpa leadRepository;

    private Lead testLead;

    @BeforeEach
    void setUp() {
        leadRepository.deleteAll();
        testLead = Lead.builder()
                .company("Megacorp")
                .email("iventalll@gmail.com")
                .phone("+7911")
                .status(LeadStatusJpa.NEW)
                .build();
        testLead = leadRepository.save(testLead);
    }

    @Test
    void demonstrate_readCommitted_annotation() {
        Lead result = demoService.readWithReadCommitted(testLead.getId());
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testLead.getId());
        System.out.println("✓ Метод с READ_COMMITTED успешно выполнился");
    }

    @Test
    void demonstrate_repeatableRead_annotation() {
        Lead result = demoService.readWithRepeatableRead(testLead.getId());
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testLead.getId());
        System.out.println("✓ Метод с REPEATABLE_READ успешно выполнился");
    }

    @Test
    void demonstrate_serializable_annotation() {
        Lead result = demoService.readWithSerializable(testLead.getId());
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testLead.getId());
        System.out.println("✓ Метод с SERIALIZABLE успешно выполнился");
    }
}

