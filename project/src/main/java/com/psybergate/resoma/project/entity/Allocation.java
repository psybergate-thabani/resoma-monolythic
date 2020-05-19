package com.psybergate.resoma.project.entity;

import com.psybergate.resoma.people.entity.Employee;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@EqualsAndHashCode(of = {"employee", "project"}, callSuper = false)
@Entity
@Table(name = "allocation", uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "employee_id"}))
public class Allocation extends BaseEntity {

    @ManyToOne()
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    private Project project;

    public Allocation(Project project, Employee employee) {
        this.project = project;
        this.employee = employee;
    }

}
