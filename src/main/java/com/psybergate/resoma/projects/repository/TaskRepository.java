package com.psybergate.resoma.projects.repository;

import com.psybergate.resoma.projects.entity.Project;
import com.psybergate.resoma.projects.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findAllByProjectAndDeleted(Project project, boolean deleted);

    Task findByCodeAndDeleted(String code, boolean deleted);

    Set<Task> findAllByCodeInAndDeleted(Set<String> taskCodes, boolean deleted);
}
