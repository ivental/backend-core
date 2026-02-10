package ru.mentee.power.crm.spring.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LeadService {
    private static final Logger log = LoggerFactory.getLogger(LeadService.class);
    private final LeadRepository repository;

    public LeadService(LeadRepository repository) {
        this.repository = repository;
        log.info("LeadService constructor called");
    }

    @PostConstruct
    void init() {
        log.info("LeadService @PostConstruct init() called - Bean lifecycle phase");
    }

    public Lead addLead(String email, String phone, String company, LeadStatus status) {
        Optional<Lead> existingLead = findByEmail(email);
        if (existingLead.isPresent()) {
            throw new IllegalStateException("email уже существует");
        }

        Lead lead = new Lead(UUID.randomUUID(), email, phone, company, status);
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

    public List<Lead> findByStatus(LeadStatus status) {
        List<Lead> filtered = repository.findAll().stream()
                .filter(lead -> lead.status().equals(status))
                .collect(Collectors.toList());

        System.out.println("Found " + filtered.size() + " leads with status " + status);
        return filtered;
    }

    public void update(UUID id, Lead updatedLead) {
        Lead existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + id));
        Lead updated = new Lead(
                existing.id(),
                updatedLead.email(),
                updatedLead.phone(),
                updatedLead.company(),
                updatedLead.status()
        );
        repository.save(updated);
    }

    public void delete(UUID id) {
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Lead with id " + id + " not found"
                ));
        repository.delete(id);
    }

    public List<Lead> findLeads(String search, LeadStatus status) {
        List<Lead> leads = repository.findAll();
        Stream<Lead> stream = leads.stream();

        if (search != null && !search.trim().isEmpty()) {
            String lowerSearch = search.toLowerCase().trim();
            stream = stream.filter(lead ->
                    lead.email().toLowerCase().contains(lowerSearch) ||
                            lead.company().toLowerCase().contains(lowerSearch)
            );
        }

        if (status != null) {
            stream = stream.filter(lead -> lead.status().equals(status));
        }

        return stream.toList();
    }
}





