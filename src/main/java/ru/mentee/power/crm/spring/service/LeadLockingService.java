package ru.mentee.power.crm.spring.service;


import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;



import java.util.UUID;


@Service
public class LeadLockingService {
    private final LeadRepositoryJpa leadRepositoryJpa;

    public LeadLockingService(LeadRepositoryJpa leadRepositoryJpa) {
        this.leadRepositoryJpa = leadRepositoryJpa;
    }

    @Transactional
    public Lead convertLeadToDealWithLock(UUID leadId, LeadStatusJpa newStatus) {
        Lead lead = leadRepositoryJpa.findByIdForUpdate(leadId)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + leadId));

        lead.setStatus(newStatus);
        return leadRepositoryJpa.save(lead);
    }

    @Transactional
    public void updateLeadStatusOptimistic(UUID leadId, LeadStatusJpa newStatus) {
        Lead lead = leadRepositoryJpa.findById(leadId)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + leadId));

        lead.setStatus(newStatus);
        leadRepositoryJpa.save(lead);
    }

    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    @Transactional
    public Lead updateWithRetry(UUID leadId, LeadStatusJpa newStatus) {
        Lead lead = leadRepositoryJpa.findById(leadId)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + leadId));
        lead.setStatus(newStatus);
        return leadRepositoryJpa.save(lead);
    }


    @Transactional
    public void processTwoLeadsInOrder(UUID leadIdFirst, UUID leadIdSecond) {
        leadRepositoryJpa.findByIdForUpdate(leadIdFirst)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + leadIdFirst));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted during processing", e);
        }
        leadRepositoryJpa.findByIdForUpdate(leadIdSecond)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + leadIdSecond));
    }
}