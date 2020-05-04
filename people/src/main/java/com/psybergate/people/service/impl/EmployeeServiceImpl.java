package com.psybergate.people.service.impl;


import com.psybergate.people.entity.Employee;
import com.psybergate.people.repository.EmployeeRepository;
import com.psybergate.people.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public Employee createEmployee(Employee employee) {
        dataValidation(employee);
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> retrieveEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> retrieveEmployees(Boolean deleted) {
        return employeeRepository.findAllByDeleted(deleted);
    }

    @Override
    public Employee retrieveEmployee(UUID employeeId) {
        return employeeRepository.findByIdAndDeleted(employeeId, false);
    }

    @Override
    public Employee retrieveEmployee(String employeeCode) {
        return employeeRepository.findByEmployeeCode(employeeCode);
    }

    @Override
    public Employee retrieveEmployee(String employeeCode, Boolean deleted) {
        return employeeRepository.findByEmployeeCodeAndDeleted(employeeCode, deleted);
    }

    @Override
    public List<Employee> retrieveEmployeesIn(List<String> employeeCodes) {
        return employeeRepository.findAllByEmployeeCodeIn(employeeCodes);
    }

    @Override
    public List<Employee> retrieveEmployeesIn(List<String> employeeCodes, Boolean deleted) {
        return employeeRepository.findAllByEmployeeCodeInAndDeleted(employeeCodes, deleted);
    }

    @Override
    @Transactional
    public Employee updateEmployee(Employee employee) {
        dataValidation(employee);
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID employeeId) {
        Employee employee = retrieveEmployee(employeeId);
        employee.setDeleted(true);
        employee = employeeRepository.save(employee);
    }

    private void dataValidation(Employee employee) {
        List<String> invalidData = new ArrayList<>();

        if (employee == null)
            throw new NullPointerException("Employee cannot be null.");

        if (employee.getFullName() == null || employee.getFullName().isEmpty())
            invalidData.add("Employee full name");

        if (employee.getLastName() == null || employee.getLastName().isEmpty())
            invalidData.add("Employee last name");

        if (employee.getEmployeeCode() == null || employee.getEmployeeCode().isEmpty())
            invalidData.add("Employee code");

        if (employee.getStartDate() == null)
            invalidData.add("Start date");

        if (employee.getOccupation() == null || employee.getOccupation().isEmpty())
            invalidData.add("Occupation");

        if (employee.getStatus() == null || employee.getStatus().isEmpty())
            invalidData.add("Status");

        if (invalidData.size() > 0)
            throw new ValidationException(invalidData + " cannot be null or empty.");
    }
}
