package com.psybergate.resoma.projects.service;

import com.psybergate.resoma.projects.entity.Project;
import com.psybergate.resoma.projects.entity.Task;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProjectService {

    Project captureProject(Project newProject);

    Project retrieveProject(UUID id);

    List<Project> retrieveProjects();

    Project updateProject(Project project);

    void addPersonToProject(String employeeCode, UUID projectId);

    void deleteProject(UUID id);

    Task addTaskToProject(Task newTask, UUID projectId);

    List<Task> retrieveTasks(Project project);

    void deleteTaskByProject(Project project);

    void deleteTask(UUID taskId);

    Task retrieveTask(UUID taskId);
}
