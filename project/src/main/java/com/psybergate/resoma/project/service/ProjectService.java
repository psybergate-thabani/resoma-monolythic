package com.psybergate.resoma.project.service;

import com.psybergate.resoma.project.entity.Allocation;
import com.psybergate.resoma.project.entity.Project;
import com.psybergate.resoma.project.entity.Task;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProjectService {

    Project captureProject(Project newProject);

    Project retrieveProject(UUID id, boolean deleted);

    List<Project> retrieveProjects(boolean deleted);

    Project updateProject(Project project);

    void deleteProject(UUID id);

    Task addTaskToProject(Task newTask, UUID projectId);

    List<Task> retrieveTasks(Project project);

    void deleteTaskByProject(Project project);

    void deleteTask(UUID taskId);

    Task retrieveTask(UUID taskId);

    Set<Allocation> retrieveAllocations(Project project);

    Allocation allocateEmployee(Allocation allocation);

    void deallocateEmployee(UUID allocationId);

    Allocation retrieveAllocation(UUID allocationId);

    Set<Allocation> retrieveAllocations(Project project, Boolean deleted);
}
