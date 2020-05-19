package com.psybergate.resoma.project.service;


import com.psybergate.resoma.project.entity.Allocation;
import com.psybergate.resoma.project.entity.Project;
import com.psybergate.resoma.project.entity.Task;
import com.psybergate.resoma.project.repository.AllocationRepository;
import com.psybergate.resoma.project.repository.ProjectRepository;
import com.psybergate.resoma.project.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

    private TaskRepository taskRepository;

    private AllocationRepository allocationRepository;

    @Autowired
    public ProjectServiceImpl(
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            AllocationRepository allocationRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.allocationRepository = allocationRepository;
    }

    @Override
    @Transactional
    public Project captureProject(@Valid Project newProject) {
        return projectRepository.save(newProject);
    }

    @Override
    public Project retrieveProject(UUID id) {
        return projectRepository.findByIdAndDeleted(id, false);
    }

    @Override
    @Transactional
    public List<Project> retrieveProjects() {
        return projectRepository.findAllByDeleted(false);
    }

    @Override
    @Transactional
    public Project updateProject(@Valid Project project) {
        return projectRepository.save(project);
    }

    @Override
    @Transactional
    public void addPersonToProject(UUID employeeId, UUID projectId) {
        Project project = retrieveProject(projectId);
//        project.getTeam().add(employeeCode);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public void deleteProject(UUID id) {
        Project project = retrieveProject(id);
        if (project == null) throw new ValidationException("Project does not exist");
        project.setDeleted(true);
        Project updatedProject = projectRepository.save(project);
        deleteTaskByProject(updatedProject);
    }

    @Override
    @Transactional
    public Task addTaskToProject(@Valid Task newTask, UUID projectId) {
        Project project = retrieveProject(projectId);
        newTask.setProject(project);
        return taskRepository.save(newTask);
    }

    @Override
    @Transactional
    public List<Task> retrieveTasks(@Valid Project project) {
        return taskRepository.findAllByProjectAndDeleted(project, false);
    }

    @Override
    @Transactional
    public void deleteTaskByProject(@Valid Project project) {
        List<Task> tasks = taskRepository.findAllByProjectAndDeleted(project, false);
        tasks.forEach(task -> {
            task.setDeleted(true);
            taskRepository.save(task);
        });
    }

    @Override
    @Transactional
    public void deleteTask(UUID taskId) {
        Task task = taskRepository.getOne(taskId);
        task.setDeleted(true);
        taskRepository.save(task);
    }

    @Override
    public Task retrieveTask(UUID taskId) {
        return taskRepository.findByIdAndDeleted(taskId, false);
    }

    @Override
    public Set<Allocation> retrieveAllocations(Project project) {
        return allocationRepository.findAllByProject(project);
    }

    @Override
    public Allocation allocateEmployee(Allocation allocation) {
        return allocationRepository.save(allocation);
    }

    @Override
    public void deallocateEmployee(UUID allocationId) {
        Allocation allocation = allocationRepository.getOne(allocationId);
        allocation.setDeleted(true);
        allocationRepository.save(allocation);
    }

    @Override
    public Allocation retrieveAllocation(UUID allocationId) {
        return allocationRepository.getOne(allocationId);
    }

    @Override
    public Set<Allocation> retrieveAllocations(Project project, Boolean deleted) {
        return allocationRepository.findAllByProjectAndDeleted(project, deleted);
    }

}
