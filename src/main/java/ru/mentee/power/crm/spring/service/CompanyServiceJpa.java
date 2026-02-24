package ru.mentee.power.crm.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mentee.power.crm.spring.model.Company;
import ru.mentee.power.crm.spring.repository.CompanyRepository;

@Service
@RequiredArgsConstructor
public class CompanyServiceJpa {
    private final CompanyRepository companyRepository;

    public Company findOrCreateByName(String name) {
        return companyRepository.findByName(name)
                .orElseGet(() -> {
                    Company company = new Company();
                    company.setName(name);
                    return companyRepository.save(company);
                });
    }
}
