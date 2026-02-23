package ru.mentee.power.crm.spring.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.spring.model.Deal;
import ru.mentee.power.crm.spring.model.DealStatusJpa;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.repository.DealRepositoryJpa;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DealServiceJpa {
    private final DealRepositoryJpa dealRepository;
    private final LeadRepositoryJpa leadRepositoryJpa;


    @Transactional
    public Deal transitionDealStatus(UUID dealId, DealStatusJpa newStatus) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new IllegalArgumentException("Deal not found: " + dealId));
        if (!deal.getStatus().canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    String.format("Cannot transition from %s to %s", deal.getStatus(), newStatus)
            );
        }

        deal.setStatus(newStatus);
        return deal;
    }

    @Transactional
    public Deal convertLeadToDeal(UUID leadId, BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Lead lead = leadRepositoryJpa.findById(leadId)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + leadId));

        Deal deal = Deal.builder()
                .leadId(leadId)
                .amount(amount)
                .title("Сделка с " + lead.getCompany())
                .status(DealStatusJpa.NEW)
                .build();
        return dealRepository.save(deal);
    }

    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    public Map<DealStatusJpa, List<Deal>> getDealsByStatusForKanban() {
        return dealRepository.findAll().stream()
                .collect(Collectors.groupingBy(Deal::getStatus));
    }
}
