package com.psybergate.resoma.project.repository;

import com.psybergate.resoma.project.entity.Allocation;
import com.psybergate.resoma.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface AllocationRepository extends JpaRepository<Allocation, UUID> {

    Set<Allocation> findAllByProject(Project project);

    Set<Allocation> findAllByProjectAndDeleted(Project project, Boolean deleted);
}
