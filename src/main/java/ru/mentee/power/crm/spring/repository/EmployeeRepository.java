package ru.mentee.power.crm.spring.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.mentee.power.crm.spring.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {}
