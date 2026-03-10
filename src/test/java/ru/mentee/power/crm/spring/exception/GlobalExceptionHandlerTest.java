package ru.mentee.power.crm.spring.exception;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import ru.mentee.power.crm.spring.repository.CompanyRepository;
import ru.mentee.power.crm.spring.repository.DealRepositoryJpa;
import ru.mentee.power.crm.spring.repository.InviteeRepository;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;
import ru.mentee.power.crm.spring.rest.LeadRestController;
import ru.mentee.power.crm.spring.rest.fixed.InviteeController;
import ru.mentee.power.crm.spring.service.CompanyServiceJpa;
import ru.mentee.power.crm.spring.service.DealServiceJpa;
import ru.mentee.power.crm.spring.service.InviteeService;
import ru.mentee.power.crm.spring.service.LeadServiceJpa;

@WebMvcTest(LeadRestController.class)
@ActiveProfiles("test")
public class GlobalExceptionHandlerTest {

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
  void shouldReturn404_whenEntityNotFound() throws Exception {
    UUID id = UUID.randomUUID();

    when(leadService.findById(id)).thenThrow(new EntityNotFoundException("Lead", id.toString()));
    mockMvc
        .perform(get("/api/leads/{id}", id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Lead not found with id: " + id))
        .andExpect(jsonPath("$.path").value("/api/leads/" + id))
        .andExpect(jsonPath("$.errors").doesNotExist());
  }

  @Test
  void shouldReturn400WithFieldErrors_whenValidationFails() throws Exception {
    String invalidJson =
        """
            {
                "email": "",
                "phone": "",
                "companyId": null
            }
            """;

    mockMvc
        .perform(post("/api/leads").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Validation failed"))
        .andExpect(jsonPath("$.path").value("/api/leads"))
        .andExpect(jsonPath("$.errors").exists())
        .andExpect(jsonPath("$.errors.companyId").value("must not be null"))
        .andExpect(jsonPath("$.errors.email").doesNotExist())
        .andExpect(jsonPath("$.errors.phone").doesNotExist());
  }

  @Test
  void shouldReturn500_whenUnexpectedExceptionOccurs() throws Exception {
    UUID id = UUID.randomUUID();
    when(leadService.findById(id)).thenThrow(new RuntimeException("Database connection failed"));
    mockMvc
        .perform(get("/api/leads/{id}", id))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.error").value("Internal Server Error"))
        .andExpect(
            jsonPath("$.message").value("Internal server error occurred. " + "Contact support."))
        .andExpect(jsonPath("$.path").value("/api/leads/" + id))
        .andExpect(jsonPath("$.errors").doesNotExist());
  }
}
