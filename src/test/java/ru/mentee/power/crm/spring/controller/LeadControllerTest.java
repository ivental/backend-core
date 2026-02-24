package ru.mentee.power.crm.spring.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.repository.CompanyRepository;
import ru.mentee.power.crm.spring.repository.DealRepositoryJpa;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;
import ru.mentee.power.crm.spring.service.DealServiceJpa;
import ru.mentee.power.crm.spring.service.LeadService;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;



@WebMvcTest(
        controllers = LeadController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {DealControllerJpa.class, DealServiceJpa.class,
                        DealRepositoryJpa.class}
        )
)
@ActiveProfiles("test")
class LeadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LeadService leadService;

    @MockitoBean
    private DealRepositoryJpa dealRepositoryJpa;

    @MockitoBean
    private LeadRepositoryJpa leadRepositoryJpa;

    @MockitoBean
    private CompanyRepository companyRepository;

    private Lead existingLead;
    private UUID validId;

    @BeforeEach
    void setUp() {
        validId = UUID.randomUUID();
        existingLead = new Lead(
                validId,
                "test@example.com",
                "+79991234567",
                "Test",
                LeadStatus.NEW
        );
    }

    @Test
    void shouldShowEditFormForExistingLead() throws Exception {
        when(leadService.findById(validId)).thenReturn(Optional.of(existingLead));
        mockMvc.perform(get("/leads/{id}/edit", validId))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/edit"))
                .andExpect(model().attribute("lead", existingLead));
    }

    @Test
    void shouldUpdateLeadAndRedirectToList() throws Exception {
        when(leadService.findById(validId)).thenReturn(Optional.of(existingLead));
        mockMvc.perform(post("/leads/{id}", validId)
                        .param("email", "updated@example.com")
                        .param("phone", "+79999999999")
                        .param("company", "Updated Corp")
                        .param("status", "LOST")
                        .param("id", validId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"));

        verify(leadService).update(eq(validId), argThat(lead ->
                "updated@example.com".equals(lead.email()) &&
                        "+79999999999".equals(lead.phone()) &&
                        "Updated Corp".equals(lead.company()) &&
                        LeadStatus.LOST.equals(lead.status())
        ));
    }

    @Test
    void shouldReturn404ForNonExistentLeadId() throws Exception {
        UUID invalidId = UUID.randomUUID();
        when(leadService.findById(invalidId)).thenReturn(Optional.empty());
        mockMvc.perform(get("/leads/{id}/edit", invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldShowCreateForm() throws Exception {
        mockMvc.perform(get("/leads/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/create"))
                .andExpect(model().attributeExists("lead"));
    }

    @Test
    void shouldCreateLeadAndRedirectToList() throws Exception {
        Lead newLead = new Lead(
                UUID.randomUUID(),
                "new@example.com",
                "+79991234567",
                "New Company",
                LeadStatus.NEW
        );
        when(leadService.addLead(
                "new@example.com",
                "+79991234567",
                "New Company",
                LeadStatus.NEW
        )).thenReturn(newLead);
        mockMvc.perform(post("/leads")
                        .param("email", "new@example.com")
                        .param("phone", "+79991234567")
                        .param("company", "New Company")
                        .param("status", "NEW")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"));
        verify(leadService).addLead(
                "new@example.com",
                "+79991234567",
                "New Company",
                LeadStatus.NEW
        );
    }
    @Test
    void shouldDeleteLeadAndRedirect() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/leads/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"));

        verify(leadService).delete(id);
    }

    @Test
    void shouldFilterBySearchTerm() throws Exception {
        String searchTerm = "test";
        Lead expectedLead = new Lead(
                UUID.randomUUID(),
                "test@example.com",
                "+79991234567",
                "Test Company",
                LeadStatus.NEW
        );
        when(leadService.findLeads((searchTerm), (null)))
                .thenReturn(List.of(expectedLead));

        mockMvc.perform(get("/leads")
                        .param("search", searchTerm))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/list"))
                .andExpect(model().attribute("leads", List.of(expectedLead)))
                .andExpect(model().attribute("search", searchTerm))
                .andExpect(model().attribute("status", ""));
    }

    @Test
    void shouldFilterByStatus() throws Exception {
        LeadStatus status = LeadStatus.NEW;
        Lead lead1 = new Lead(UUID.randomUUID(), "a@example.com", "123", "A", status);
        Lead lead2 = new Lead(UUID.randomUUID(), "b@example.com", "456", "B", status);
        List<Lead> expectedLeads = List.of(lead1, lead2);
        when(leadService.findLeads((null), (status)))
                .thenReturn(expectedLeads);

        mockMvc.perform(get("/leads")
                        .param("status", status.name()))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/list"))
                .andExpect(model().attribute("leads", expectedLeads))
                .andExpect(model().attribute("search", ""))
                .andExpect(model().attribute("status", status.name()));
    }

    @Test
    void shouldReturnAllLeadsWhenNoParameters() throws Exception {
        Lead lead1 = new Lead(UUID.randomUUID(), "a@example.com", "123", "A", LeadStatus.NEW);
        Lead lead2 = new Lead(UUID.randomUUID(), "b@example.com", "456", "B", LeadStatus.CONTACTED);
        List<Lead> allLeads = List.of(lead1, lead2);
        when(leadService.findLeads((null), null))
                .thenReturn(allLeads);

        mockMvc.perform(get("/leads"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/list"))
                .andExpect(model().attribute("leads", allLeads))
                .andExpect(model().attribute("search", ""))
                .andExpect(model().attribute("status", ""));
    }

    @Test
    void shouldCombineSearchAndStatusFilters() throws Exception {
        String searchTerm = "acme";
        LeadStatus status = LeadStatus.NEW;
        Lead expectedLead = new Lead(
                UUID.randomUUID(),
                "contact@acme.com",
                "+79990000000",
                "Acme Inc",
                status
        );
        when(leadService.findLeads((searchTerm), (status)))
                .thenReturn(List.of(expectedLead));

        mockMvc.perform(get("/leads")
                        .param("search", searchTerm)
                        .param("status", status.name()))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/list"))
                .andExpect(model().attribute("leads", List.of(expectedLead)))
                .andExpect(model().attribute("search", searchTerm))
                .andExpect(model().attribute("status", status.name()));
    }
    @Test
    void shouldNotCreateLeadWithEmptyEmail() throws Exception {
        mockMvc.perform(post("/leads")
                        .param("email", "")
                        .param("phone", "+79119633911")
                        .param("company", "Megacorp")
                        .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/create"))
                .andExpect(model().attributeHasFieldErrors("lead", "email"));
    }

    @Test
    void shouldNotCreateLeadWithInvalidEmail() throws Exception {
        mockMvc.perform(post("/leads")
                        .param("email", "iventalllgmailcom")
                        .param("phone", "+79119633911")
                        .param("company", "Megacorp")
                        .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/create"))
                .andExpect(model().attributeHasFieldErrors("lead", "email"));
    }

    @Test
    void shouldNotCreateLeadWithEmptyPhone() throws Exception {
        mockMvc.perform(post("/leads")
                        .param("email", "iventalll@gmail.com")
                        .param("phone", "")
                        .param("company", "Megacorp")
                        .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/create"))
                .andExpect(model().attributeHasFieldErrors("lead", "phone"));
    }
    @Test
    void shouldNotCreateLeadWithEmptyCompany() throws Exception {
        mockMvc.perform(post("/leads")
                        .param("email", "iventalll@gmail.com")
                        .param("phone", "+79119633911")
                        .param("company", "")
                        .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/create"))
                .andExpect(model().attributeHasFieldErrors("lead", "company"));
    }
    @Test
    void shouldNotCreateLeadWithoutStatus() throws Exception {
        mockMvc.perform(post("/leads")
                                .param("email", "iventalll@gmail.com")
                                .param("phone", "+79119633911")
                                .param("company", "Megacorp")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("leads/create"))
                .andExpect(model().attributeHasFieldErrors("lead", "status"));
    }
}