package com.psybergate.resoma.project.repository;

import com.psybergate.resoma.project.entity.Allocation;
import com.psybergate.resoma.project.entity.Project;
import com.psybergate.resoma.project.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    Project findByIdAndDeleted(UUID id, boolean deleted);

    List<Project> findAllByDeleted(boolean deleted);

    Project findFirstByAllocationsId(UUID id);

    Project findFirstByTasks_id(UUID id);
}
