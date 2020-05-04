package com.psybergate.people.repository;


import com.psybergate.people.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Employee findByIdAndDeleted(UUID id, boolean deleted);

    List<Employee> findAllByDeleted(boolean deleted);

    Employee findByEmployeeCodeAndDeleted(String employeeCode, boolean deleted);

    List<Employee> findAllByEmployeeCodeInAndDeleted(List<String> employeeCodes, boolean deleted);

    Employee findByEmployeeCode(String employeeCode);

    List<Employee> findAllByEmployeeCodeIn(List<String> employeeCodes);
}
