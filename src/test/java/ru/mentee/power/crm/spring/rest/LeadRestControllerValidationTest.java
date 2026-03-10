package ru.mentee.power.crm.spring.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.repository.CompanyRepository;
import ru.mentee.power.crm.spring.repository.DealRepositoryJpa;
import ru.mentee.power.crm.spring.repository.InviteeRepository;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;
import ru.mentee.power.crm.spring.rest.fixed.InviteeController;
import ru.mentee.power.crm.spring.service.CompanyServiceJpa;
import ru.mentee.power.crm.spring.service.DealServiceJpa;
import ru.mentee.power.crm.spring.service.InviteeService;
import ru.mentee.power.crm.spring.service.LeadServiceJpa;

@WebMvcTest()
@ActiveProfiles("test")
public class LeadRestControllerValidationTest {
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
  void shouldReturn400_whenEmailIsInvalidFormat() throws Exception {
    CreateLeadRequest request = new CreateLeadRequest();
    request.setEmail("not-an-email");
    request.setPhone("+79119633911");
    request.setCompanyId(UUID.randomUUID());
    String requestJson = objectMapper.writeValueAsString(request);
    mockMvc
        .perform(post("/api/leads").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn400_whenPhoneIsTooLong() throws Exception {
    CreateLeadRequest request = new CreateLeadRequest();
    request.setEmail("iventall@gmail");
    request.setPhone("+123456789101112131415");
    request.setCompanyId(UUID.randomUUID());
    String requestJson = objectMapper.writeValueAsString(request);
    mockMvc
        .perform(post("/api/leads").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn400_whenCompanyIdIsNull() throws Exception {
    CreateLeadRequest request = new CreateLeadRequest();
    request.setEmail("iventalll@gmail");
    request.setPhone("+79119633911");
    request.setCompanyId(null);
    String requestJson = objectMapper.writeValueAsString(request);
    mockMvc
        .perform(post("/api/leads").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn201_whenAllFieldsAreValid() throws Exception {
    UUID companyId = UUID.randomUUID();
    UUID leadId = UUID.randomUUID();
    CreateLeadRequest request = new CreateLeadRequest();
    request.setEmail("iv@gmail.com");
    request.setPhone("+79119633911");
    request.setCompanyId(companyId);
    Lead mockLead = Lead.builder().id(leadId).email("iv@gmail.com").phone("+79119633911").build();
    when(leadService.createLead(any(Lead.class))).thenReturn(mockLead);
    String requestJson = objectMapper.writeValueAsString(request);
    mockMvc
        .perform(post("/api/leads").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.id").value(leadId.toString()))
        .andExpect(jsonPath("$.email").value("iv@gmail.com"))
        .andExpect(jsonPath("$.phone").value("+79119633911"));
  }
}
