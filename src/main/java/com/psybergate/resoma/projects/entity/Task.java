package com.psybergate.resoma.projects.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode(of = "code")
@ToString(callSuper = true)
@Entity(name = "task")
public class Task extends BaseEntity {

    @NotBlank(message = "Task code is mandatory")
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotNull(message = "Task name is mandatory")
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public Task() {
    }

    public Task(String code, String name, Project project, boolean deleted) {
        this.code = code;
        this.name = name;
        this.project = project;
        this.setDeleted(deleted);
    }
}
