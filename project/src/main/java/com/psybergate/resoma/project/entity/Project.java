package com.psybergate.resoma.project.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "code", callSuper = false)
@Entity(name = "project")
public class Project extends BaseEntity {

    @NotBlank(message = "{projectcode.notblank}")
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotBlank(message = "{projectname.notblank}")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "{clientcode.notblank}")
    @Column(name = "client_code", nullable = false)
    private String clientCode;

    @NotNull(message = "{startdate.notnull}")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectType type;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Set<Allocation> allocations = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Set<Task> tasks = new HashSet<>();

    public Project() {
    }

    public Project(String code, String name, String clientCode, LocalDate startDate, LocalDate endDate, ProjectType type) {
        this.code = code;
        this.name = name;
        this.clientCode = clientCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        super.setId(UUID.randomUUID());
        super.setCreatedDate(LocalDateTime.now());
    }

    public void setDeleted(boolean deleted) {
        super.setDeleted(deleted);
        if (deleted == true) {
            allocations.stream().forEach(allocation -> allocation.setDeleted(true));
            tasks.stream().forEach(task -> task.setDeleted(true));
        }
    }

    public Project copyUpdatableFields(Project project) {
        setName(project.getName());
        setCode(project.getCode());
        setStartDate(project.getStartDate());
        setEndDate(project.getEndDate());
        setType(project.getType());
        return this;
    }

    public boolean addAllocation(@Valid Allocation allocation) {
        return allocations.add(allocation);
    }

    public void removeAllocation(UUID allocationId) {
        for (Allocation allocation : allocations) {
            if (Objects.equals(allocation.getId(), allocationId)) {
                allocation.setDeleted(true);
                break;
            }
        }
    }

    public boolean addTask(@Valid Task task) {
        return tasks.add(task);
    }

    public void removeTask(UUID taskId) {
        for (Task task : tasks) {
            if (Objects.equals(task.getId(), taskId)) {
                task.setDeleted(true);
                break;
            }
        }
    }

    public Allocation getAllocation(UUID allocationId) {
        for (Allocation allocation : allocations) {
            if (allocationId.equals(allocation.getId()))
                return allocation;
        }
        return null;
    }

    public Task getTask(UUID taskId) {
        for (Task task : tasks) {
            if (taskId.equals(task.getId()))
                return task;
        }
        return null;
    }
}
