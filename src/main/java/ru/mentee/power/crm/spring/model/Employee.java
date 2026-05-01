package ru.mentee.power.crm.spring.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Employee {
  @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
  private String name;
  private BigDecimal salary;
}
