package com.psybergate.resoma.projects.repository;

import com.psybergate.resoma.projects.entity.Allocation;
import com.psybergate.resoma.projects.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface AllocationRepository extends JpaRepository<Allocation, UUID> {

    Set<Allocation> findAllocationByProject(Project project);

}
