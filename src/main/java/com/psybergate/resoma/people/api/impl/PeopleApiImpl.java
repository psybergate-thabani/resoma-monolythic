package com.psybergate.resoma.people.api.impl;

import com.psybergate.resoma.people.api.PeopleApi;
import com.psybergate.resoma.people.entity.Employee;
import com.psybergate.resoma.people.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PeopleApiImpl implements PeopleApi {

    private EmployeeService employeeService;

    @Autowired
    public PeopleApiImpl(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public Employee getEmployee(UUID employeeId) {
        return employeeService.retrieveEmployee(employeeId);
    }
}
