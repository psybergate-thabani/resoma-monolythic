package com.psybergate.time.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"employeeCode", "projectCode", "taskCode", "date"})
@Entity(name = "TimeEntry")
public class TimeEntry extends BaseEntity {

    @Column(name = "employee_code", nullable = false)
    private String employeeCode;

    @Column(name = "project_code", nullable = false)
    private String projectCode;

    @Column(name = "task_code", nullable = false)
    private String taskCode;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "status_updated_by", nullable = false)
    private String statusUpdatedBy;

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

    public TimeEntry(String employeeCode, String projectCode, String taskCode, String description, int period, LocalDate date, boolean deleted) {
        this.employeeCode = employeeCode;
        this.projectCode = projectCode;
        this.taskCode = taskCode;
        this.description = description;
        this.period = period;
        this.date = date;
        this.status = Status.NEW;
        this.statusUpdatedBy = employeeCode;
        this.setDeleted(deleted);
        super.generate();
    }

    public boolean isApproved() {
        return Status.APPROVED.equals(status);
    }

}
