package ru.mentee.power.crm.spring.rest;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.mentee.power.crm.spring.dto.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.LeadResponse;
import ru.mentee.power.crm.spring.dto.UpdateLeadRequest;
import ru.mentee.power.crm.spring.exception.EntityNotFoundException;
import ru.mentee.power.crm.spring.mapper.LeadMapper;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.service.LeadServiceJpa;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
@Validated
public class LeadRestController {

  private final LeadServiceJpa leadService;
  private final LeadMapper leadMapper;

  @GetMapping
  public ResponseEntity<List<LeadResponse>> getAllLeads() {
    List<LeadResponse> responses =
        leadService.findAll().stream().map(leadMapper::toResponse).toList();
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<LeadResponse> getLeadById(@PathVariable UUID id) {
    Lead lead = leadService.findById(id);
    LeadResponse response = leadMapper.toResponse(lead);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<LeadResponse> createLead(@Valid @RequestBody CreateLeadRequest request) {
    var lead = leadMapper.toEntity(request);
    var savedLead = leadService.createLead(lead);
    var response = leadMapper.toResponse(savedLead);
    URI location = URI.create("/api/leads/" + savedLead.getId());

    return ResponseEntity.created(location).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<LeadResponse> updateLead(
      @PathVariable UUID id, @Valid @RequestBody UpdateLeadRequest request) {

    try {
      Lead existingLead = leadService.findById(id);
      leadMapper.updateEntity(request, existingLead);
      Lead updatedLead = leadService.updateLead(id, existingLead);
      return ResponseEntity.ok(leadMapper.toResponse(updatedLead));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
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
