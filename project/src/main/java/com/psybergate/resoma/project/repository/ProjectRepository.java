package com.psybergate.resoma.project.repository;

import com.psybergate.resoma.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    Project findByIdAndDeleted(UUID id, boolean deleted);

    List<Project> findAllByDeleted(boolean deleted);
}
