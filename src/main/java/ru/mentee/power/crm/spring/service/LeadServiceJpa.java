package ru.mentee.power.crm.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

@RequiredArgsConstructor
public class LeadServiceJpa {

    private final LeadRepositoryJpa repository;


    public Lead addLead(Lead lead) {
        if (repository.findByEmailNative(lead.getEmail()).isPresent()) {
            throw new IllegalStateException("Lead with email already exists: " + lead.getEmail());
        }
        return repository.save(lead);
    }


    public List<Lead> findAll() {
        return repository.findAll();
    }


    public Optional<Lead> findById(UUID id) {
        return repository.findById(id);
    }


    public Optional<Lead> findByEmail(String email) {
        return repository.findByEmailNative(email);
    }


    public List<Lead> findByStatus(LeadStatusJpa status) {
        return repository.findByStatusNative(status.name());
    }


    public void update(UUID id, Lead updatedLead) {
        Lead existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + id));

        existing.setEmail(updatedLead.getEmail());
        existing.setPhone(updatedLead.getPhone());
        existing.setCompany(updatedLead.getCompany());
        existing.setStatus(updatedLead.getStatus());

        repository.save(existing);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }


    public List<Lead> findLeads(String search, LeadStatusJpa status) {
        List<Lead> allLeads = repository.findAll();

        if (status != null) {
            allLeads = allLeads.stream()
                    .filter(lead -> lead.getStatus() == status)
                    .collect(Collectors.toList());
        }

        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase().trim();
            allLeads = allLeads.stream()
                    .filter(lead ->
                            lead.getEmail().toLowerCase().contains(searchLower) ||
                                    lead.getCompany().toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());
        }

        return allLeads;
    }
}