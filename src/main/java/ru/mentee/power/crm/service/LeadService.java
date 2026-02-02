package ru.mentee.power.crm.service;

import org.springframework.stereotype.Service;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LeadService {
    private final LeadRepository repository;

    public LeadService(LeadRepository repository) {
        this.repository = repository;
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
        List<Lead> allLeads = repository.findAll();
        List<Lead> result = allLeads.stream()
                .filter(lead -> {
                    boolean matches = lead.status().equals(status);
                    System.out.println("  " + lead.email() + " -> status=" + lead.status() + ", matches=" + matches);
                    return matches;
                })
                .collect(Collectors.toList());
        return result;
    }
}

