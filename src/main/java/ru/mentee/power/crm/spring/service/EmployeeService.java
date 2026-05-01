package ru.mentee.power.crm.spring.service;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.spring.model.Employee;
import ru.mentee.power.crm.spring.repository.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class EmployeeService {
  private final EmployeeRepository repository;

  @Transactional
  public Employee add(Employee employee) {
    return repository.save(employee);
  }

  @Transactional
  public Employee updateSalary(UUID id, BigDecimal salary) {
    Employee employee =
        repository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    employee.setSalary(salary);
    return repository.save(employee);
  }

  @Transactional(readOnly = true)
  public Page<Employee> getAll(Pageable pageable) {
    return repository.findAll(pageable);
  }

  @Transactional
  public void delete(UUID id) {
    repository.deleteById(id);
  }
}
