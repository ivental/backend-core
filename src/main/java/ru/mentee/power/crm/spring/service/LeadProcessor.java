package ru.mentee.power.crm.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;

import java.util.UUID;


@Setter
@RequiredArgsConstructor
public class LeadProcessor {
    private final LeadRepositoryJpa repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processSingleLead(UUID leadId) {
        Lead lead = repository.findById(leadId)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + leadId));

        if (lead.getEmail().contains("fail")) {
            throw new RuntimeException("Simulated failure for lead: " + leadId);
        }

        lead.setStatus(LeadStatusJpa.CONTACTED);
        repository.save(lead);
    }
}
