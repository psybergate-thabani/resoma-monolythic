package com.psybergate.resoma.project.entity;

import com.psybergate.resoma.people.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Entity
@Table(name = "allocation", uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "employee_id"}))
public class Allocation extends BaseEntity {

    @NotNull(message = "{employee.notblank}")
    @ManyToOne()
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
