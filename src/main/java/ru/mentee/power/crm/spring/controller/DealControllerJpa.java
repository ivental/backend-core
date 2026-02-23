package ru.mentee.power.crm.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.DealStatusJpa;
import ru.mentee.power.crm.spring.service.DealServiceJpa;
import ru.mentee.power.crm.spring.service.LeadServiceJpa;
import java.math.BigDecimal;
import java.util.UUID;

@Controller
@RequestMapping("/jpa-deals")
public class DealControllerJpa {
    private final DealServiceJpa dealService;
    private final LeadServiceJpa leadService;

    public DealControllerJpa(DealServiceJpa dealService, LeadServiceJpa leadService) {
        this.dealService = dealService;
        this.leadService = leadService;
    }

    @GetMapping
    public String listDeals(Model model) {
        model.addAttribute("deals", dealService.getAllDeals());
        return "jpa-deals/list";
    }

    @GetMapping("/kanban")
    public String kanbanView(Model model) {
        model.addAttribute("dealsByStatus", dealService.getDealsByStatusForKanban());
        return "jpa-deals/kanban";
    }

    @GetMapping("/convert/{leadId}")
    public String showConvertForm(@PathVariable UUID leadId, Model model) {
        Lead lead = leadService.findById(leadId)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + leadId));
        model.addAttribute("lead", lead);
        return "jpa-deals/convert";
    }

    @PostMapping("/convert")
    public String convertLeadToDeal(@RequestParam UUID leadId, @RequestParam BigDecimal amount) {
        dealService.convertLeadToDeal(leadId, amount);
        return "redirect:/jpa-deals";
    }

    @PostMapping("/{id}/transition")
    public String transitionStatus(@PathVariable UUID id, @RequestParam DealStatusJpa newStatus) {
        dealService.transitionDealStatus(id, newStatus);
        return "redirect:/jpa-deals/kanban";
    }
}
