package com.psybergate.resoma.people.api;

import com.psybergate.resoma.people.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    Employee createEmployee(Employee employee);

    List<Employee> retrieveEmployees(Boolean deleted);

    Employee retrieveEmployee(UUID employeeId);

    Employee updateEmployee(Employee employee);

    void deleteEmployee(UUID employeeId);
}
