package com.psybergate.people.service;

import com.psybergate.people.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    Employee createEmployee(Employee employee);

    List<Employee> retrieveEmployees();

    List<Employee> retrieveEmployees(Boolean deleted);

    Employee retrieveEmployee(UUID employeeId);

    Employee retrieveEmployee(String employeeCode);

    Employee retrieveEmployee(String employeeCode, Boolean deleted);

    List<Employee> retrieveEmployeesIn(List<String> employeeCodes);

    List<Employee> retrieveEmployeesIn(List<String> employeeCodes, Boolean deleted);

    Employee updateEmployee(Employee employee);

    void deleteEmployee(UUID employeeId);

}
