package com.psybergate.resoma.projects.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "code")
@ToString(callSuper = true, exclude = "tasks")
@Entity(name = "project")
public class Project extends BaseEntity {

    @NotBlank(message = "Project code is mandatory")
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Client code is mandatory")
    @Column(name = "client_code", nullable = false)
    private String clientCode;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = true)
    private LocalDate endDate;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Allocation> allocations = new HashSet<>();

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectType type;

    public Project() {
    }

    public Project(String code, String name, String clientCode, LocalDate startDate, LocalDate endDate, ProjectType type) {
        this.code = code;
        this.name = name;
        this.clientCode = clientCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

}
