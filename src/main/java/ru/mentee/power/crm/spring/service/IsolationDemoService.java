package ru.mentee.power.crm.spring.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IsolationDemoService {
    private final LeadRepositoryJpa leadRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Lead readWithReadCommitted(UUID leadId) {
        return leadRepository.findById(leadId)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found"));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Lead readWithRepeatableRead(UUID leadId) {
        return leadRepository.findById(leadId)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found"));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Lead readWithSerializable(UUID leadId) {
        return leadRepository.findById(leadId)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found"));
    }
}
