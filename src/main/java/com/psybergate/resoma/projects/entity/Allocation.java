package com.psybergate.resoma.projects.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.psybergate.resoma.people.entity.Employee;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode(of = {"employee", "project"}, callSuper = false)
@Entity
@Table(name = "allocation")
public class Allocation extends BaseEntity {

    @ManyToOne()
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @JsonIgnore
    @ManyToOne
    private Project project;

}
