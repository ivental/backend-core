package ru.mentee.power.crm.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        if (repository.findByEmail(lead.getEmail()).isPresent()) {
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
        return repository.findByEmail(email);
    }


    public List<Lead> findByStatus(LeadStatusJpa status) {
        return repository.findByStatus(status);
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

    public List<Lead> findByStatuses(LeadStatusJpa... statuses) {
        return repository.findByStatusIn(List.of(statuses));
    }

    public Page<Lead> getFirstPage(int pageSize) {
        PageRequest pageRequest = PageRequest.of(
                0,
                pageSize,
                Sort.by("createdAt").descending()
        );
        return repository.findAll(pageRequest);
    }

    public Page<Lead> searchByCompany(String company, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByCompany(company, pageable);
    }

    @Transactional
    public int convertNewToContacted() {
        int updated = repository.updateStatusBulk(LeadStatusJpa.NEW, LeadStatusJpa.CONTACTED);
        System.out.printf("Converted %d leads from NEW to CONTACTED%n", updated);
        return updated;
    }

    @Transactional
    public int archiveOldLeads(LeadStatusJpa status) {
        return repository.deleteByStatusBulk(status);
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