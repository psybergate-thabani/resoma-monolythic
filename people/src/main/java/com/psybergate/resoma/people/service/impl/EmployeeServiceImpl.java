package com.psybergate.resoma.people.service.impl;


import com.psybergate.resoma.people.entity.Employee;
import com.psybergate.resoma.people.repository.EmployeeRepository;
import com.psybergate.resoma.people.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(@Valid EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public Employee createEmployee(@Valid Employee employee) {
        return employeeRepository.save(employee);
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
    @Transactional
    public Employee updateEmployee(@Valid Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID employeeId) {
        Employee employee = retrieveEmployee(employeeId);
        employee.setDeleted(true);
        employeeRepository.save(employee);
    }

}
