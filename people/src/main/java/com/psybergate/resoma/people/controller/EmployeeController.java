package com.psybergate.resoma.people.controller;

import com.psybergate.resoma.people.entity.Employee;
import com.psybergate.resoma.people.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;

@RequestMapping(path = "api/people")
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("v1/employees")
    public ResponseEntity<Employee> captureEmployee(@RequestBody @Valid Employee employee) {
        return ResponseEntity.ok(employeeService.createEmployee(employee));
    }

    @PutMapping("v1/employees/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@RequestBody @Valid Employee employee, @PathVariable UUID employeeId) {

        if ((employee == null || employeeId == null) || !employeeId.equals(employee.getId()))
            throw new ValidationException("id in url path must match employee id in request body");

        return ResponseEntity.ok(employeeService.updateEmployee(employee));
    }

    @GetMapping(value = "v1/employees", params = {"deleted"})
    public ResponseEntity<List<Employee>> retrieveEmployees(@RequestParam("deleted") Boolean deleted) {
        return ResponseEntity.ok(employeeService.retrieveEmployees(deleted));
    }

    @GetMapping("v1/employees/{employeeId}")
    public ResponseEntity<Employee> retrieveEmployee(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(employeeService.retrieveEmployee(employeeId));
    }

    @DeleteMapping("v1/employees/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok().build();
    }
}