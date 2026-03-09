package ru.mentee.power.crm.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateLeadRequest {
  @Email(message = "{lead.email.email}")
  @Size(max = 100, message = "{lead.email.size}")
  private String email;

  @Size(max = 20, message = "{lead.phone.size}")
  private String phone;

  private UUID companyId;

  private LeadStatusJpa status;
}
