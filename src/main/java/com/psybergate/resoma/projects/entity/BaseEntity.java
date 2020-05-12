package com.psybergate.resoma.projects.entity;

import lombok.*;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = "id")
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "version")
    @Version
    private Long version;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "created")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated")
    private LocalDateTime updated;

    @Column(name = "updated_by")
    private String updatedBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @PrePersist
    protected void prePersist() {
        this.id = UUID.randomUUID();
        this.createdDate = LocalDateTime.now();
    }
}
