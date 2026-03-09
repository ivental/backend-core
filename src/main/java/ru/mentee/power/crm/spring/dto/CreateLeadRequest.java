package ru.mentee.power.crm.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLeadRequest {
  @NotBlank(message = "Email обязателен")
  @Email(message = "Email должен быть в корректном формате")
  @Size(max = 100, message = "{lead.email.size}")
  private String email;

  @NotBlank(message = "{lead.phone.notblank}")
  @Size(max = 20, message = "{lead.phone.size}")
  private String phone;

  @NotNull(message = "{company.id.notnull}")
  private UUID companyId;
}
