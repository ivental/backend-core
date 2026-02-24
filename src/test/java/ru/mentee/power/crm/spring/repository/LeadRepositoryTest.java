package ru.mentee.power.crm.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.spring.model.Company;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class LeadRepositoryTest {

    @Autowired
    private LeadRepositoryJpa repository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void shouldSaveAndFindLeadById_whenValidData() {
        Company company = Company.builder()
                .name("Megacorp")
                .build();
        company = companyRepository.save(company);
        Lead lead = Lead.builder()
                .email("iventalio@gmail.com")
                .phone("+7911")
                .status(LeadStatusJpa.NEW)
                .build();

        lead.setCompany(company);


        Lead saved = repository.save(lead);
        Optional<Lead> found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("iventalio@gmail.com");
    }

    @Test
    void shouldFindByEmailNative_whenLeadExists() {

        Company company = Company.builder()
                .name("Megacorp")
                .build();
        company = companyRepository.save(company);

        Lead lead = Lead.builder()
                .email("ivi@gmail.com")
                .phone("+7911")
                .status(LeadStatusJpa.NEW)
                .build();
        lead.setCompany(company);
        repository.save(lead);
        Optional<Lead> found = repository.findByEmail("ivi@gmail.com");
        assertThat(found).isPresent();
        assertThat(found.get().getCompany().getName()).isEqualTo("Megacorp");
    }

    @Test
    void shouldReturnEmptyOptional_whenEmailNotFound() {
        Optional<Lead> found = repository.findByEmail("nonexistent@test.com");
        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindAllLeads() {
        Lead lead = Lead.builder()

                .email("iventalll@gmail.com")
                .phone("+7911")
                .status(LeadStatusJpa.NEW)
                .build();
        Lead leadFirst = Lead.builder()

                .email("iventalll1@gmail.com")
                .phone("+7911")
                .status(LeadStatusJpa.NEW)
                .build();
        repository.save(lead);
        repository.save(leadFirst);
        var all = repository.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    void shouldDeleteLead() {
        Lead lead = Lead.builder()

                .email("iventalll@gmail.com")
                .phone("+7911")
                .status(LeadStatusJpa.NEW)
                .build();
        repository.save(lead);
        repository.deleteById(lead.getId());
        assertThat(repository.findById(lead.getId())).isEmpty();
    }
}
