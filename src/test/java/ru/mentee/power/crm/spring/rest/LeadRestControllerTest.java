package ru.mentee.power.crm.spring.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
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
import ru.mentee.power.crm.spring.model.Company;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import ru.mentee.power.crm.spring.repository.CompanyRepository;
import ru.mentee.power.crm.spring.repository.DealRepositoryJpa;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;
import ru.mentee.power.crm.spring.service.CompanyServiceJpa;
import ru.mentee.power.crm.spring.service.DealServiceJpa;
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
    when(leadService.findById(id)).thenReturn(Optional.empty());

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
    when(leadService.findById(id)).thenReturn(Optional.of(lead));

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
    Lead lead =
        Lead.builder()
            .email("ive@gmail.com")
            .phone("+7911")
            .company(Company.builder().id(UUID.randomUUID()).build())
            .status(LeadStatusJpa.NEW)
            .build();

    Lead createdLead =
        Lead.builder()
            .id(id)
            .email(lead.getEmail())
            .phone(lead.getPhone())
            .company(lead.getCompany())
            .status(lead.getStatus())
            .createdAt(OffsetDateTime.now())
            .version(0L)
            .build();

    when(leadService.createLead(any(Lead.class))).thenReturn(createdLead);

    mockMvc
        .perform(
            post("/api/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lead)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/api/leads/" + id))
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.email").value("ive@gmail.com"));
  }

  @Test
  void shouldReturn404_whenUpdateNonExistentLead() throws Exception {
    UUID id = UUID.randomUUID();
    Lead leadToUpdate =
        Lead.builder()
            .email("ivi@gmail.com")
            .phone("+7912")
            .status(LeadStatusJpa.CONTACTED)
            .build();

    when(leadService.updateLead(any(UUID.class), any(Lead.class))).thenReturn(Optional.empty());

    mockMvc
        .perform(
            put("/api/leads/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leadToUpdate)))
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
