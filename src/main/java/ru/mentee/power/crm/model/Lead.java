package ru.mentee.power.crm.model;

import java.util.UUID;

public record Lead(UUID id, String email, String phone, String company, LeadStatus status) {
}
