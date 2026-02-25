package ru.mentee.power.crm.spring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.spring.model.Company;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.service.CompanyServiceJpa;
import ru.mentee.power.crm.spring.service.LeadServiceJpa;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequestMapping("/jpa-leads")
@RequiredArgsConstructor
public class LeadControllerJpa {

    private final LeadServiceJpa leadService;
    private final CompanyServiceJpa companyService;


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
                .company(new Company())
                .status(LeadStatusJpa.NEW)
                .build();

        System.out.println("=== DEBUG ===");
        System.out.println("Lead object: " + lead);
        System.out.println("Lead email: " + lead.getEmail());
        System.out.println("Lead company: " + lead.getCompany());
        System.out.println("Lead company name: " + (lead.getCompany() != null ? lead.getCompany().getName() : "null"));


        model.addAttribute("lead", lead);
        return "jpa-leads/create";
    }

    @PostMapping
    public String createLead(
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String company,
            @RequestParam LeadStatusJpa status,
            Model model) {

        System.out.println("Creating lead with:");
        System.out.println("  email: " + email);
        System.out.println("  phone: " + phone);
        System.out.println("  companyName: " + company);
        System.out.println("  status: " + status);

        try {
            Company companyEntity = companyService.findOrCreateByName(company);

            Lead lead = Lead.builder()
                    .email(email)
                    .phone(phone)
                    .company(companyEntity)
                    .status(status)
                    .build();

            leadService.addLead(lead);
            return "redirect:/jpa-leads";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ошибка при создании лида: " + e.getMessage());
            return "jpa-leads/create";
        }
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


