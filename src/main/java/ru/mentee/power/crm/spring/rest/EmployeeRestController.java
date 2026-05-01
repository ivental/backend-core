package ru.mentee.power.crm.spring.rest;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.mentee.power.crm.spring.dto.CreateEmployeeRequest;
import ru.mentee.power.crm.spring.dto.EmployeeResponse;
import ru.mentee.power.crm.spring.dto.UpdateSalaryRequest;
import ru.mentee.power.crm.spring.model.Employee;
import ru.mentee.power.crm.spring.service.EmployeeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeRestController {
  private final EmployeeService employeeService;

  @PostMapping
  public EmployeeResponse add(@RequestBody CreateEmployeeRequest request) {
    Employee employee = toEntity(request);
    Employee saved = employeeService.add(employee);
    return new EmployeeResponse(saved.getId(), saved.getName(), saved.getSalary());
  }

  @PutMapping("/{id}/salary")
  public EmployeeResponse updateSalary(@PathVariable UUID id, @RequestBody UpdateSalaryRequest request) {
    Employee updated = employeeService.updateSalary(id, request.getSalary());
    return new EmployeeResponse(updated.getId(), updated.getName(), updated.getSalary());
  }

  @GetMapping
  public Page<EmployeeResponse> getAll(Pageable pageable) {
    return employeeService.getAll(pageable)
        .map(emp -> new EmployeeResponse(emp.getId(), emp.getName(), emp.getSalary()));
  }

  private Employee toEntity(CreateEmployeeRequest request) {
    return new Employee(null, request.getName(), request.getSalary());
  }
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    employeeService.delete(id);
  }
}
