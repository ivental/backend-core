package ru.mentee.power.crm.spring.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.spring.controller.DealController;
import ru.mentee.power.crm.spring.controller.DealControllerJpa;
import ru.mentee.power.crm.spring.controller.LeadControllerJpa;
import ru.mentee.power.crm.spring.dto.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.UpdateLeadRequest;
import ru.mentee.power.crm.spring.exception.EntityNotFoundException;
import ru.mentee.power.crm.spring.model.Company;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import ru.mentee.power.crm.spring.repository.CompanyRepository;
import ru.mentee.power.crm.spring.repository.DealRepositoryJpa;
import ru.mentee.power.crm.spring.repository.InviteeRepository;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;
import ru.mentee.power.crm.spring.rest.fixed.InviteeController;
import ru.mentee.power.crm.spring.service.CompanyServiceJpa;
import ru.mentee.power.crm.spring.service.DealServiceJpa;
import ru.mentee.power.crm.spring.service.InviteeService;
import ru.mentee.power.crm.spring.service.LeadServiceJpa;

@WebMvcTest(LeadRestController.class)
@ActiveProfiles("test")
public class LeadRestControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private LeadServiceJpa leadService;

  @MockitoBean private DealServiceJpa dealService;

  @MockitoBean private CompanyServiceJpa companyService;

  @MockitoBean private LeadRepositoryJpa leadRepository;

  @MockitoBean private DealRepositoryJpa dealRepository;

  @MockitoBean private CompanyRepository companyRepository;

  @MockitoBean private DealControllerJpa dealControllerJpa;

  @MockitoBean private DealController dealController;

  @MockitoBean private LeadControllerJpa leadControllerJpa;

  @MockitoBean private InviteeController inviteeController;

  @MockitoBean private InviteeRepository inviteeRepository;

  @MockitoBean private InviteeService inviteeService;

  @Test
  void shouldReturn200_whenGetAllLeads() throws Exception {
    List<Lead> leads =
        List.of(
            Lead.builder()
                .id(UUID.randomUUID())
                .email("iv@gmail.com")
                .phone("+7911")
                .status(LeadStatusJpa.NEW)
                .createdAt(OffsetDateTime.now())
                .build());
    when(leadService.findAll()).thenReturn(leads);

    mockMvc
        .perform(get("/api/leads"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].email").value("iv@gmail.com"));
  }

  @Test
  void shouldReturn404_whenGetNonExistentLead() throws Exception {
    UUID id = UUID.randomUUID();
    when(leadService.findById(id)).thenThrow(new EntityNotFoundException("Lead", id.toString()));
    mockMvc.perform(get("/api/leads/{id}", id)).andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn200_whenGetExistingLead() throws Exception {
    UUID id = UUID.randomUUID();
    Lead lead =
        Lead.builder()
            .id(id)
            .email("iv@gmail.com")
            .phone("+7911")
            .status(LeadStatusJpa.NEW)
            .createdAt(OffsetDateTime.now())
            .build();
    when(leadService.findById(id)).thenReturn(lead);

    mockMvc
        .perform(get("/api/leads/{id}", id))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.email").value("iv@gmail.com"));
  }

  @Test
  void shouldReturn201WithLocation_whenCreateLead() throws Exception {
    UUID id = UUID.randomUUID();
    UUID companyId = UUID.randomUUID();
    CreateLeadRequest request = new CreateLeadRequest();
    request.setEmail("ive@gmail.com");
    request.setPhone("+7911");
    request.setCompanyId(companyId);

    Lead createdLead =
        Lead.builder()
            .id(id)
            .email(request.getEmail())
            .phone(request.getPhone())
            .company(Company.builder().id(companyId).build())
            .status(LeadStatusJpa.NEW)
            .createdAt(OffsetDateTime.now())
            .version(0L)
            .build();

    when(leadService.createLead(any(Lead.class))).thenReturn(createdLead);

    mockMvc
        .perform(
            post("/api/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/api/leads/" + id))
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.email").value("ive@gmail.com"));
  }

  @Test
  void shouldReturn404_whenUpdateNonExistentLead() throws Exception {
    UUID id = UUID.randomUUID();

    when(leadService.findById(id)).thenThrow(new EntityNotFoundException("Lead", id.toString()));

    UpdateLeadRequest request = new UpdateLeadRequest();
    request.setEmail("ivi@gmail.com");
    request.setPhone("+7912");
    request.setStatus(LeadStatusJpa.CONTACTED);
    request.setCompanyId(UUID.randomUUID()); // если нужно

    mockMvc
        .perform(
            put("/api/leads/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))) // ← отправляем request
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn204_whenDeleteExistingLead() throws Exception {
    UUID id = UUID.randomUUID();
    when(leadService.deleteLead(id)).thenReturn(true);

    mockMvc.perform(delete("/api/leads/{id}", id)).andExpect(status().isNoContent());
  }

  @Test
  void shouldReturn404_whenDeleteNonExistentLead() throws Exception {
    UUID id = UUID.randomUUID();
    when(leadService.deleteLead(id)).thenReturn(false);

    mockMvc.perform(delete("/api/leads/{id}", id)).andExpect(status().isNotFound());
  }
}
