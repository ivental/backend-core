package ru.mentee.power.crm.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.spring.model.Company;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")

public class CompanyRepositoryTest {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private LeadRepositoryJpa leadRepository;

    @Test
    void shouldSaveCompanyWithLeads() {

        Company company = Company.builder()
                .name("Сберанк")
                .industry("Финансы")
                .build();

        Lead leadFirst = Lead.builder()
                .email("ilia@sber.com")
                .phone("+7911")
                .company(company)
                .status(LeadStatusJpa.NEW)
                .build();

        Lead leadSecond = Lead.builder()
                .email("maria@sber.com")
                .phone("+7911")
                .company(company)
                .status(LeadStatusJpa.CONTACTED)
                .build();

        Lead leadThird = Lead.builder()
                .email("artem@sber.com")
                .phone("+7911")
                .company(company)
                .status(LeadStatusJpa.CONTACTED)
                .build();

        company.addLead(leadFirst);
        company.addLead(leadSecond);
        company.addLead(leadThird);

        Company saved = companyRepository.save(company);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getLeads()).hasSize(3);

        Company found = companyRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getLeads()).hasSize(3);
    }

    @Test
    void shouldAvoidN1WithEntityGraph() {
        Company company = Company.builder()
                .name("Яндекс")
                .industry("IT")
                .build();
        company = companyRepository.save(company);

        for (int i = 0; i < 5; i++) {
            Lead lead = Lead.builder()
                    .email("lead" + i + "@yandex.ru")
                    .phone("+7911" + i)
                    .status(LeadStatusJpa.NEW)
                    .build();

            company.addLead(lead);
            leadRepository.save(lead);
        }

        Company found = companyRepository.findByIdWithLeads(company.getId()).orElseThrow();
        assertThat(found.getLeads()).hasSize(5);
    }
}
