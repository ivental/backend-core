package ru.mentee.power.crm.spring.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
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
