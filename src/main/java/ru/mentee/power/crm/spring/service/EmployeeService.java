package ru.mentee.power.crm.spring.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mentee.power.crm.spring.model.Employee;
import ru.mentee.power.crm.spring.repository.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class EmployeeService {
  private final EmployeeRepository repository;

  public Employee add(Employee employee) {
    return repository.save(employee);
  }

  public Employee updateSalary(UUID id, BigDecimal salary) {
    Employee employee =
        repository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    employee.setSalary(salary);
    return repository.save(employee);
  }

  public List<Employee> getAll() {
    return repository.findAll();
  }

  public void delete(UUID id) {
    repository.deleteById(id);
  }
}
