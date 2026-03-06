package ru.mentee.power.crm.spring.rest;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;
import ru.mentee.power.crm.spring.service.LeadServiceJpa;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadRestController {
  private final LeadServiceJpa leadService;
  private final LeadRepositoryJpa leadRepositoryJpa;

  @GetMapping
  public ResponseEntity<List<Lead>> getAllLeads() {
    List<Lead> leads = leadService.findAll();
    return ResponseEntity.ok(leads);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Lead> getLeadById(@PathVariable UUID id) {
    return leadService
        .getLeadById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Lead> createLead(@RequestBody Lead lead) {
    Lead createdLead = leadService.createLead(lead);
    URI location = URI.create("/api/leads/" + createdLead.getId());
    return ResponseEntity.created(location).body(createdLead);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Lead> updateLead(@PathVariable UUID id, @RequestBody Lead lead) {
    return leadService
        .updateLead(id, lead)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLead(@PathVariable UUID id) {
    boolean deleted = leadService.deleteLead(id);
    if (deleted) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
