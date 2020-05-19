package com.psybergate.resoma.project.repository;


import com.psybergate.resoma.project.entity.Project;
import com.psybergate.resoma.project.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findAllByProjectAndDeleted(Project project, boolean deleted);

    Task findByIdAndDeleted(UUID taskId, boolean b);
}
