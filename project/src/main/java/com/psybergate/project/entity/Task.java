package com.psybergate.project.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = "code")
@ToString(callSuper = true)
@Entity(name = "task")
public class Task extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true)
    private String code;

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
        this.setId(UUID.randomUUID());
    }
}
