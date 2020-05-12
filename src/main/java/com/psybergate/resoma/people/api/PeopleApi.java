package com.psybergate.resoma.people.api;

import com.psybergate.resoma.people.entity.Employee;

import java.util.UUID;
/**
 * API exposed to communicate with the people module
 * */
public interface PeopleApi {

    Employee getEmployee(UUID employeeId);
}
