package com.psybergate.resoma.people.service;

import com.psybergate.resoma.people.api.EmployeeService;
import com.psybergate.resoma.people.entity.Employee;
import com.psybergate.resoma.people.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = new Employee("emp1", "John", "Doe", "JohnD@resoma.com", "78 Home Address, Johannesburg",
                "79 Postal Address, Johannesburg", LocalDateTime.now(), LocalDate.now(), "Developer", "Active");
    }

    @Test
    void shouldCreateNewEmployee_whenEmployeeIsCreated() {
        //Arrange
        when(employeeRepository.save(employee)).thenReturn(employee);

        //Act
        Employee savedEmployee = employeeService.createEmployee(employee);

        //Assert
        assertNotNull(savedEmployee);
        assertEquals(employee, savedEmployee);
    }

    @Test
    void shouldReturnListOfEmployees_WhenRetrievingEmployees() {
        //Arrange
        Employee employeeA = new Employee("empA", "John", "Doe", "JohnD@resoma.com", "78 Home Address, Johannesburg",
                "79 Postal Address, Johannesburg", LocalDateTime.now(), LocalDate.now(), "Developer", "Active");
        Employee employeeB = new Employee("empB", "John", "Doe", "JohnD@resoma.com", "78 Home Address, Johannesburg",
                "79 Postal Address, Johannesburg", LocalDateTime.now(), LocalDate.now(), "Developer", "Active");
        Employee employeeC = new Employee("empC", "John", "Doe", "JohnD@resoma.com", "78 Home Address, Johannesburg",
                "79 Postal Address, Johannesburg", LocalDateTime.now(), LocalDate.now(), "Developer", "Active");
        List<Employee> employees = Arrays.asList(employeeA, employeeB, employeeC);
        when(employeeRepository.findAllByDeleted(true)).thenReturn(employees);

        //Act
        List<Employee> resultList = employeeService.retrieveEmployees(true);

        //Assert
        assertEquals(3, resultList.size());
        assertEquals(employees, resultList);
        resultList.forEach(e -> assertTrue(resultList.contains(e)));
        verify(employeeRepository, times(1)).findAllByDeleted(true);
    }

    @Test
    void shouldReturnEmployee_whenEmployeeIsRetrieved() {
        //Arrange
        UUID id = employee.getId();
        when(employeeRepository.findByIdAndDeleted(id, false)).thenReturn(employee);

        //Act
        Employee resultEmployee = employeeService.retrieveEmployee(id);

        //Assert
        assertNotNull(resultEmployee);
        assertEquals(employee, resultEmployee);
        verify(employeeRepository, times(1)).findByIdAndDeleted(id, false);
    }

    @Test
    void shouldUpdateEmployee_whenEmployeeIsUpdated() {
        //Arrange
        when(employeeRepository.save(employee)).thenReturn(employee);

        //Act
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //Assert
        assertNotNull(updatedEmployee);
        assertEquals(employee, updatedEmployee);
    }

    @Test
    void shouldDeleteEmployee_whenEmployeeIsDeleted() {
        //Arrange
        UUID id = employee.getId();
        when(employeeRepository.findByIdAndDeleted(id, false)).thenReturn(employee);

        //Act
        employeeService.deleteEmployee(id);

        //Assert
        verify(employeeRepository, times(1)).save(employee);
    }
}