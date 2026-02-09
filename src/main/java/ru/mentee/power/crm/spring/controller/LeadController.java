package ru.mentee.power.crm.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor

public class LeadController {
    private final LeadService leadService;

    @GetMapping("/leads/{id}/edit")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Optional <Lead> optionalLead = leadService.findById(id);
        if (optionalLead.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Lead not found");
        }
        Lead lead = optionalLead.get();
        model.addAttribute("lead", lead);
        return "leads/edit";
    }

    @PostMapping("/leads/{id}")
    public String updateLead(@PathVariable UUID id, @ModelAttribute Lead lead) {
        if (!id.equals(lead.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ID в пути (" + id + ") не совпадает с ID в объекте (" + lead.id() + ")");
        }
        leadService.update(id, lead);
        return "redirect:/leads";
    }

    @PostMapping("/leads/{id}/delete")
    public String deleteLead(@PathVariable UUID id) {
        leadService.delete(id);
        return "redirect:/leads";
    }

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "Spring Boot CRM is running! Beans created: " + leadService.findAll().size() + " leads.";
    }


    @GetMapping("/leads")
    public String showLeads(
            @RequestParam(required = false) LeadStatus status,
            Model model
    ) {

        List<Lead> list = (status == null)
                ? leadService.findAll()
                : leadService.findByStatus(status);
        model.addAttribute("leads", list);
        model.addAttribute("currentFilter", status);
        return "leads/list";
    }


    @GetMapping("/leads/new")
    public String showCreateForm(Model model) {
        model.addAttribute("lead", new Lead(null, "", "", "", LeadStatus.NEW));
        return "leads/create";
    }


    @PostMapping("/leads")
    public String createLead(@ModelAttribute Lead lead) {
        leadService.addLead(lead.email(), lead.phone(), lead.company(), lead.status());
        return "redirect:/leads";
    }
}
