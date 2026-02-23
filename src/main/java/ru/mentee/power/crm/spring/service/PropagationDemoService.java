package ru.mentee.power.crm.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropagationDemoService {

    private final LeadRepositoryJpa leadRepository;


    @Transactional(propagation = Propagation.REQUIRED)
    public void methodWithRequired(UUID leadId) {
        updateLeadStatus(leadId, LeadStatusJpa.CONTACTED);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void methodWithRequiresNew(UUID leadId) {
        updateLeadStatus(leadId, LeadStatusJpa.QUALIFIED);
    }


    @Transactional(propagation = Propagation.MANDATORY)
    public void methodWithMandatory(UUID leadId) {
        updateLeadStatus(leadId, LeadStatusJpa.LOST);
    }

    private void updateLeadStatus(UUID leadId, LeadStatusJpa status) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found"));
        lead.setStatus(status);
        leadRepository.save(lead);
    }
}
