package com.psybergate.resoma.time.entity;

import com.psybergate.resoma.people.entity.Employee;
import com.psybergate.resoma.projects.entity.Task;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"employee", "project", "task", "date"})
@Entity(name = "TimeEntry")
public class TimeEntry extends BaseEntity {

    @NotNull(message = "Employee is mandatory")
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @NotNull(message = "Task is mandatory")
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "status_reason")
    private String statusReason;

    @Column(name = "description")
    private String description;

    @Positive(message = "Period must be greater than zero")
    @Column(name = "period", nullable = false)
    private int period;

    @NotNull(message = "Date is mandatory")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    public TimeEntry() {
    }

    public TimeEntry(Employee employee, Task task, String description, int period, LocalDate date, boolean deleted) {
        this.employee = employee;
        this.task = task;
        this.description = description;
        this.period = period;
        this.date = date;
        this.status = Status.NEW;
        this.setDeleted(deleted);
        super.generate();
    }

    public boolean isApproved() {
        return Status.APPROVED.equals(status);
    }

}
