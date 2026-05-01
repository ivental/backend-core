package ru.mentee.power.crm.spring.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSalaryRequest {
  @NotNull(message = "Salary is required")
  private BigDecimal salary;
}
