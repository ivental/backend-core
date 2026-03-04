package ru.mentee.power.crm.spring.rest;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
  public List<Lead> getAllLeads() {
    return leadRepositoryJpa.findAll();
  }

  @GetMapping("/{id}")
  public Lead getLeadById(@PathVariable UUID id) {
    return leadService.findById(id).orElse(null);
  }

  @PostMapping
  public Lead createLead(@RequestBody Lead lead) {
    return leadService.addLead(lead);
  }
}
