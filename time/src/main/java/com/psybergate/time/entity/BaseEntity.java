package com.psybergate.time.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "version", nullable = false)
    @Version
    private Long version;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated")
    private LocalDateTime updated;

    @Column(name = "updated_by")
    private String updatedBy;

    public BaseEntity() {
    }

    @PrePersist
    protected void generate() {
        this.id = UUID.randomUUID();
        this.created = LocalDateTime.now();
    }

    @PreUpdate
    protected void setUpdated() {
        this.updated = LocalDateTime.now();
    }
}
