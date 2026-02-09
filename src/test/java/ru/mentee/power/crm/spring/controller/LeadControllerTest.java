package ru.mentee.power.crm.spring.controller;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
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

@WebMvcTest(LeadController.class)
class LeadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LeadService leadService;

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
}