package ru.mentee.power.crm.spring.dto;

import java.time.OffsetDateTime;
import java.util.UUID;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;

public record LeadResponse(
    UUID id,
    String email,
    String phone,
    UUID companyId,
    LeadStatusJpa status,
    OffsetDateTime createdAt) {}
