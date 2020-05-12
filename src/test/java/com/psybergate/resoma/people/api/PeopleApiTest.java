package com.psybergate.resoma.people.api;

import com.psybergate.resoma.people.api.impl.PeopleApiImpl;
import com.psybergate.resoma.people.entity.Employee;
import com.psybergate.resoma.people.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeopleApiTest {

    @Mock
    private EmployeeService employeeService;
    private PeopleApi peopleApi;

    @BeforeEach
    private void setUp() {
        peopleApi = new PeopleApiImpl(employeeService);
    }

    @Test
    void shouldReturnEmployee_whenEmployeeIsRetrievedWithId() {
        //Arrange
        Employee employee = new Employee("emp1", "John", "Doe", "JohnD@resoma.com", "78 Home Address, Johannesburg",
                "79 Postal Address, Johannesburg", LocalDateTime.now(), LocalDate.now(), "Developer", "Active");
        UUID employeeId = employee.getId();
        when(employeeService.retrieveEmployee(employeeId)).thenReturn(employee);

        //Act
        Employee resultEmployee = peopleApi.getEmployee(employeeId);

        //Assert
        assertNotNull(resultEmployee);
        assertNotNull(employeeId);
        assertEquals(employee, resultEmployee);
        verify(employeeService, times(1)).retrieveEmployee(employeeId);
    }
}