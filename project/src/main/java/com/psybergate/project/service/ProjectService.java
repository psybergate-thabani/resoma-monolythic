package com.psybergate.project.service;

import com.psybergate.project.entity.Project;
import com.psybergate.project.entity.Task;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProjectService {

    Project captureProject(Project newProject);

    Project retrieveProject(UUID id);

    Project retrieveProject(String code);

    List<Project> retrieveProjects();

    Project updateProject(Project project);

    void addPersonToProject(String employeeCode, UUID projectId);

    void deleteProject(UUID id);

    List<Project> retrieveProjects(List<String> projectCodes, boolean deleted);

    void removeDeletedEmployeeFromProjects(String employeeCode);

    Task retrieveTaskByCode(String code, boolean deleted);

    void deleteTask(Task task);

    Task addTaskToProject(Task newTask, UUID projectId);

    List<Task> retrieveTasks(Project project, boolean deleted);

    void deleteTaskByProject(Project project);

    void deleteTask(UUID taskId);

    Set<Task> retrieveTasksByCodes(Set<String> taskCodes);
}
