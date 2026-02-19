package ru.mentee.power.crm.spring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.service.LeadServiceJpa;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequestMapping("/jpa-leads")
@RequiredArgsConstructor
public class LeadControllerJpa {

    private final LeadServiceJpa leadService;

    @GetMapping
    public String showLeads(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            Model model) {

        LeadStatusJpa statusEnum = null;
        if (status != null && !status.isEmpty()) {
            statusEnum = LeadStatusJpa.valueOf(status);
        }

        List<Lead> leads = leadService.findLeads(search, statusEnum);
        model.addAttribute("leads", leads);
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("status", status != null ? status : "");
        model.addAttribute("currentFilter", statusEnum);
        return "jpa-leads/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Lead lead = Lead.builder()
                .id(null)
                .email("")
                .phone("")
                .company("")
                .status(LeadStatusJpa.NEW)
                .build();
        model.addAttribute("lead", lead);
        return "jpa-leads/create";
    }

    @PostMapping
    public String createLead(
            @Valid @ModelAttribute Lead lead,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("errors", result);
            return "jpa-leads/create";
        }
        leadService.addLead(lead);
        return "redirect:/jpa-leads";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Lead lead = leadService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Lead not found"));
        model.addAttribute("lead", lead);
        return "jpa-leads/edit";
    }

    @PostMapping("/{id}")
    public String updateLead(
            @PathVariable UUID id,
            @Valid @ModelAttribute Lead lead,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result);
            return "jpa-leads/edit";
        }
        leadService.update(id, lead);
        return "redirect:/jpa-leads";
    }

    @PostMapping("/{id}/delete")
    public String deleteLead(@PathVariable UUID id) {
        leadService.delete(id);
        return "redirect:/jpa-leads";
    }
}


