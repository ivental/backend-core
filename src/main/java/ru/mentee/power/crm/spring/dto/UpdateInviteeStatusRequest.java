package ru.mentee.power.crm.spring.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateInviteeStatusRequest(@NotBlank String status) {}
