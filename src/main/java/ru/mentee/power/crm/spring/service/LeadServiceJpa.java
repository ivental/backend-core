package ru.mentee.power.crm.spring.service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.spring.client.EmailValidationFeignClient;
import ru.mentee.power.crm.spring.client.EmailValidationResponse;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;

@Service
@RequiredArgsConstructor
public class LeadServiceJpa {

  private final LeadRepositoryJpa repository;
  private static final Logger log = LoggerFactory.getLogger(LeadServiceJpa.class);
  private final EmailValidationFeignClient emailValidationClient;


  public Lead addLead(Lead lead) {
    if (repository.findByEmail(lead.getEmail()).isPresent()) {
      throw new IllegalStateException("Lead with email already exists: " + lead.getEmail());
    }
    return repository.save(lead);
  }

  public List<Lead> findAll() {
    return repository.findAll();
  }

  public Optional<Lead> getLeadById(UUID id) {
    return repository.findById(id);
  }

  @Retry(name = "email-validation", fallbackMethod = "createLeadFallback")
  public Lead createLead(Lead lead) {
    EmailValidationResponse validation =
            emailValidationClient.validateEmail(lead.getEmail());
    if (!validation.valid()) {
      throw new IllegalArgumentException(
              "Invalid email: " + validation.reason());
    }
    lead.setCreatedAt(OffsetDateTime.now());
    return repository.save(lead);
  }

  public Lead createLeadFallback(Lead lead, Exception ex) {

    log.warn("Email validation service unavailable after retries. " +
            "Creating lead without validation. Error: {}", ex.getMessage());
    lead.setCreatedAt(OffsetDateTime.now());
    return repository.save(lead);
  }

  public Optional<Lead> updateLead(UUID id, Lead updatedLead) {
    return repository.findById(id)
            .map(existingLead -> {
              existingLead.setEmail(updatedLead.getEmail());
              existingLead.setPhone(updatedLead.getPhone());
              existingLead.setCompany(updatedLead.getCompany());
              existingLead.setStatus(updatedLead.getStatus());
              return repository.save(existingLead);
            });
  }

  public boolean deleteLead(UUID id) {
    if (repository.existsById(id)) {
      repository.deleteById(id);
      return true;
    }
    return false;
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
    Lead existing =
        repository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + id));

    existing.setCompany(updatedLead.getCompany());
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
    PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by("createdAt").descending());
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
      allLeads =
          allLeads.stream().filter(lead -> lead.getStatus() == status).collect(Collectors.toList());
    }

    if (search != null && !search.trim().isEmpty()) {
      String searchLower = search.toLowerCase().trim();
      allLeads =
          allLeads.stream()
              .filter(
                  lead ->
                      lead.getEmail().toLowerCase().contains(searchLower)
                          || lead.getCompany().getName().toLowerCase().contains(searchLower))
              .collect(Collectors.toList());
    }

    return allLeads;
  }

  @Transactional
  public void processLeads(List<UUID> leadIds) {
    for (UUID id : leadIds) {
      this.processSingleLead(id);
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void processSingleLead(UUID leadId) {
    Lead lead =
        repository
            .findById(leadId)
            .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + leadId));
    if (lead.getEmail().contains("fail")) {
      throw new RuntimeException("Simulated failure for lead: " + leadId);
    }
    lead.setStatus(LeadStatusJpa.CONTACTED);
    repository.save(lead);
  }
}
