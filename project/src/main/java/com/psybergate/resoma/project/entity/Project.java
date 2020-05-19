package com.psybergate.resoma.project.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
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

}
