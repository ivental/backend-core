package ru.mentee.power.crm.spring.rest;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.mentee.power.crm.spring.dto.generated.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.generated.LeadResponse;
import ru.mentee.power.crm.spring.dto.generated.UpdateLeadRequest;
import ru.mentee.power.crm.spring.exception.EntityNotFoundException;
import ru.mentee.power.crm.spring.mapper.LeadMapper;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.rest.generated.LeadManagementApi;
import ru.mentee.power.crm.spring.service.CompanyServiceJpa;
import ru.mentee.power.crm.spring.service.LeadServiceJpa;

@RestController
@RequiredArgsConstructor
public class LeadRestController implements LeadManagementApi {

  private final LeadServiceJpa leadService;
  private final LeadMapper leadMapper;
  private final CompanyServiceJpa companyService;

  @Override
  public ResponseEntity<List<LeadResponse>> getLeads() {
    List<LeadResponse> responses =
        leadService.findAll().stream()
            .map(leadMapper::toGeneratedResponse) // ✅ используем маппер
            .collect(Collectors.toList());
    return ResponseEntity.ok(responses);
  }

  @Override
  public ResponseEntity<LeadResponse> createLead(@Valid CreateLeadRequest createLeadRequest) {
    Lead lead = leadMapper.toEntityFromGenerated(createLeadRequest);

    var company = companyService.findById(createLeadRequest.getCompanyId());
    lead.setCompany(company);

    Lead savedLead = leadService.createLead(lead);
    LeadResponse response = leadMapper.toGeneratedResponse(savedLead);

    URI location = URI.create("/api/leads/" + savedLead.getId());
    return ResponseEntity.created(location).body(response);
  }

  @Override
  public ResponseEntity<LeadResponse> getLeadById(UUID id) {
    try {
      Lead lead = leadService.findById(id);
      LeadResponse response = leadMapper.toGeneratedResponse(lead);
      return ResponseEntity.ok(response);
    } catch (EntityNotFoundException e) {
      throw e;
    }
  }

  @Override
  public ResponseEntity<LeadResponse> updateLead(
      UUID id, @Valid UpdateLeadRequest updateLeadRequest) {
    try {
      Lead existingLead = leadService.findById(id);
      leadMapper.updateEntityFromGenerated(updateLeadRequest, existingLead);
      Lead updatedLead = leadService.updateLead(id, existingLead);
      LeadResponse response = leadMapper.toGeneratedResponse(updatedLead);
      return ResponseEntity.ok(response);
    } catch (EntityNotFoundException e) {
      throw e;
    }
  }

  @Override
  public ResponseEntity<Void> deleteLead(UUID id) {
    boolean deleted = leadService.deleteLead(id);
    if (deleted) {
      return ResponseEntity.noContent().build();
    } else {
      throw new EntityNotFoundException("Lead", id.toString());
    }
  }
}
