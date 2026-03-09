package ru.mentee.power.crm.spring.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mentee.power.crm.spring.model.Company;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import ru.mentee.power.crm.spring.service.CompanyServiceJpa;
import ru.mentee.power.crm.spring.service.LeadServiceJpa;

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
    Lead lead =
        Lead.builder()
            .id(null)
            .email("")
            .phone("")
            .company(new Company())
            .status(LeadStatusJpa.NEW)
            .build();

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

    try {
      Company companyEntity = companyService.findOrCreateByName(company);

      Lead lead =
          Lead.builder().email(email).phone(phone).company(companyEntity).status(status).build();

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
    Lead lead =
        leadService
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Lead not found"));
    model.addAttribute("lead", lead);
    return "jpa-leads/edit";
  }

  @PostMapping("/{id}")
  public String updateLead(
      @PathVariable UUID id,
      @RequestParam String email,
      @RequestParam String phone,
      @RequestParam String company,
      @RequestParam LeadStatusJpa status,
      Model model) {

    try {
      Company companyEntity = companyService.findOrCreateByName(company);
      Lead updatedLead =
          Lead.builder()
              .id(id)
              .email(email)
              .phone(phone)
              .company(companyEntity)
              .status(status)
              .build();

      leadService.update(id, updatedLead);
      return "redirect:/jpa-leads";

    } catch (Exception e) {
      e.printStackTrace();
      model.addAttribute("error", "Ошибка при обновлении лида: " + e.getMessage());
      Lead existingLead =
          leadService
              .findById(id)
              .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Lead not found"));
      model.addAttribute("lead", existingLead);
      return "jpa-leads/edit";
    }
  }

  @PostMapping("/{id}/delete")
  public String deleteLead(@PathVariable UUID id) {
    leadService.delete(id);
    return "redirect:/jpa-leads";
  }

  @GetMapping("/update-email")
  public String showUpdateEmailsForm(Model model) {
    model.addAttribute("companyName", "");
    model.addAttribute("newEmail", "");
    return "jpa-leads/update-email";
  }

  @PostMapping("/update-email")
  public String updateEmails(
      @RequestParam String companyName,
      @RequestParam String newEmail,
      RedirectAttributes redirectAttributes) { // RedirectAttributes вместо Model

    int count = leadService.updateEmailsByCompanyName(companyName, newEmail);

    if (count > 0) {
      redirectAttributes.addFlashAttribute(
          "message", String.format("Обновлено %d лидов компании '%s'", count, companyName));
    } else {
      redirectAttributes.addFlashAttribute(
          "message", String.format("Лиды компании '%s' не найдены", companyName));
    }

    return "redirect:/jpa-leads/update-email"; // Редирект на GET
  }
}

// без bulk операции - второй способ данной задачи, сравнить оба способа и решить какой из них
// удобнее
