package com.psybergate.resoma.project.api;

import com.psybergate.resoma.project.entity.Allocation;
import com.psybergate.resoma.project.entity.Project;
import com.psybergate.resoma.project.entity.Task;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProjectService {

    Project captureProject(Project newProject);

    Project retrieveProject(UUID projectId, boolean deleted);

    List<Project> retrieveProjects(boolean deleted);

    Project updateProject(Project project);

    void deleteProject(UUID projectId);

    Task addTaskToProject(Task newTask, UUID projectId);

    Set<Task> retrieveTasks(UUID projectId, boolean deleted);

    void deleteTask(UUID taskId);

    Task retrieveTask(UUID taskId);

    Set<Allocation> retrieveAllocations(UUID projectId);

    Allocation allocateEmployee(UUID projectId, Allocation allocation);

    void deallocateEmployee(UUID allocationId);

    Allocation retrieveAllocation(UUID allocationId);

    Set<Allocation> retrieveAllocations(UUID projectId, Boolean deleted);

    Allocation reallocateEmployee(UUID allocationId);
}