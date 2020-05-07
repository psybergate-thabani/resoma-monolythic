package com.psybergate.resoma.time.entity;

import com.psybergate.resoma.people.entity.Employee;
import com.psybergate.resoma.projects.entity.Project;
import com.psybergate.resoma.projects.entity.Task;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"employee", "project", "task", "date"})
@Entity(name = "TimeEntry")
public class TimeEntry extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

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

    @Column(name = "period", nullable = false)
    private int period;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    public TimeEntry() {
    }

    public TimeEntry(Employee employee, Project project, Task task, String description, int period, LocalDate date, boolean deleted) {
        this.employee = employee;
        this.project = project;
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
