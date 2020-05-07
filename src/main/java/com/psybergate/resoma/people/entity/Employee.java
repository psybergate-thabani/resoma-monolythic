package com.psybergate.resoma.people.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = "employeeCode")
@Entity(name = "Employee")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Employee extends BaseEntity {

    @NotBlank(message = "Employee code is mandatory")
    @Column(name = "employee_code", nullable = false, unique = true)
    private String employeeCode;

    @NotBlank(message = "Full name is mandatory")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotBlank(message = "Last name is mandatory")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email is not valid")
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank(message = "Physical address is mandatory")
    @Column(name = "physical_address", nullable = false)
    private String physicalAddress;

    @NotBlank(message = "Postal address is mandatory")
    @Column(name = "postal_address", nullable = false)
    private String postalAddress;

    @NotNull(message = "Start date is mandatory")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotBlank(message = "Occupation is mandatory")
    @Column(name = "occupation", nullable = false)
    private String occupation;

    @Column(name = "status")
    private String status;

    public Employee() {
    }

    public Employee(String employeeCode, String fullName, String lastName, String email, String physicalAddress,
                    String postalAddress, LocalDateTime createDate, LocalDate startDate, String occupation, String status) {
        this.employeeCode = employeeCode;
        this.fullName = fullName;
        this.lastName = lastName;
        this.email = email;
        this.physicalAddress = physicalAddress;
        this.postalAddress = postalAddress;
        this.startDate = startDate;
        this.occupation = occupation;
        this.status = status;
        this.setCreatedDate(createDate);
    }
}
