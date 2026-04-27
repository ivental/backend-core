package ru.mentee.power.crm.spring.rest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mentee.power.crm.spring.model.Employee;
import ru.mentee.power.crm.spring.service.EmployeeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeRestController {
  private final EmployeeService employeeService;

  @PostMapping
  public Employee add(@RequestBody Employee employee) {
    return employeeService.add(employee);
  }

  @PutMapping("/{id}/salary")
  public Employee updateSalary(@PathVariable UUID id, @RequestParam BigDecimal salary) {
    return employeeService.updateSalary(id, salary);
  }

  @GetMapping
  public List<Employee> getAll() {
    return employeeService.getAll();
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    employeeService.delete(id);
  }
}
