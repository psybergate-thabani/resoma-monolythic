package com.psybergate.resoma.projects.entity;

import com.psybergate.resoma.people.entity.Employee;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "code")
@ToString(callSuper = true, exclude = "tasks")
@Entity(name = "project")
public class Project extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "client_code", nullable = false)
    private String clientCode;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = true)
    private LocalDate endDate;

    @ManyToMany
    @JoinTable(
            name = "project_team",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<Employee> team = new HashSet<>();

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectType type;

    public Project() {
    }

    public Project(String code, String name, String clientCode, LocalDate startDate, LocalDate endDate, Set<Employee> team, ProjectType type) {
        this.code = code;
        this.name = name;
        this.clientCode = clientCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.team = team;
        this.type = type;
    }

}
