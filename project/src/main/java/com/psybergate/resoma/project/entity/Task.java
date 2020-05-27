package com.psybergate.resoma.project.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = "code", callSuper = false)
@ToString(callSuper = true)
@Entity(name = "task")
public class Task extends BaseEntity {

    @NotBlank(message = "{taskcode.notblank}")
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotNull(message = "{taskname.notblank}")
    @Column(name = "name", nullable = false)
    private String name;

    public Task() {
    }

    public Task(String code, String name, boolean deleted) {
        this.code = code;
        this.name = name;
        this.setDeleted(deleted);
        super.setId(UUID.randomUUID());
        super.setCreatedDate(LocalDateTime.now());
    }
}
