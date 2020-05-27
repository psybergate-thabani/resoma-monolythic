package com.psybergate.resoma.project.service;


import com.psybergate.resoma.project.entity.Allocation;
import com.psybergate.resoma.project.entity.Project;
import com.psybergate.resoma.project.entity.Task;
import com.psybergate.resoma.project.repository.ProjectRepository;
import org.hibernate.service.UnknownServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.lang.model.UnknownEntityException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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
        Project savedProject = projectRepository.getOne(project.getId());
        savedProject.copyUpdatableFields(project);
        return projectRepository.save(savedProject);
    }

    @Override
    @Transactional
    public void deleteProject(UUID id) {
        Project project = projectRepository.getOne(id);
        checkNull(project == null, "Project does not exist");
        project.setDeleted(true);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public Task addTaskToProject(@Valid Task newTask, UUID projectId) {
        Project project = projectRepository.getOne(projectId);
        project.addTask(newTask);
        project = projectRepository.save(project);
        for (Task task: project.getTasks()) {
            if(task.getCode().equals(newTask.getCode()))
                return task;
        }
        throw new IllegalArgumentException();
    }

    @Override
    @Transactional
    public Set<Task> retrieveTasks(@Valid UUID projectId, boolean deleted) {
        Project project = projectRepository.getOne(projectId);
        Set<Task> tasks = project.getTasks().stream().filter(task -> task.isDeleted() == deleted)
                .collect(Collectors.toSet());
        return tasks;
    }

    @Override
    @Transactional
    public void deleteTask(UUID taskId) {
        Project project = projectRepository.findFirstByTasks_id(taskId);
        project.removeTask(taskId);
        projectRepository.save(project);
    }

    @Override
    public Task retrieveTask(UUID taskId) {
        Project project =  projectRepository.findFirstByTasks_id(taskId);
        return project.getTask(taskId);
    }

    @Transactional
    @Override
    public Set<Allocation> retrieveAllocations(UUID projectId) {
        Project project = projectRepository.getOne(projectId);
        return project.getAllocations();
    }

    @Transactional
    @Override
    public Allocation allocateEmployee(UUID projectId, @Valid Allocation allocation) {
        Project project = projectRepository.getOne(projectId);
        project.addAllocation(allocation);
        project = projectRepository.save(project);
        for (Allocation tempAllocation : project.getAllocations()) {
            if (allocation.getEmployee().equals(tempAllocation.getEmployee())) {
                allocation = tempAllocation;
            }
        }
        return allocation;
    }

    @Transactional
    @Override
    public void deallocateEmployee(UUID allocationId) {
        Project project = projectRepository.findFirstByAllocationsId(allocationId);
        project.removeAllocation(allocationId);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public Allocation retrieveAllocation(UUID allocationId) {
        Project project = projectRepository.findFirstByAllocationsId(allocationId);
        return project.getAllocation(allocationId);
    }

    @Override
    @Transactional
    public Set<Allocation> retrieveAllocations(UUID projectId, Boolean deleted) {
        Project project = projectRepository.getOne(projectId);
        Set<Allocation> allocations = project.getAllocations().stream()
                .filter(allocation -> allocation.isDeleted() == deleted)
                .collect(Collectors.toSet());
        return allocations;
    }

    @Override
    public Allocation reallocateEmployee(@Valid UUID allocationId) {
        Project project = projectRepository.findFirstByAllocationsId(allocationId);
        checkNull(Objects.isNull(project), "Project does not exists");
        project.removeAllocation(allocationId);
        projectRepository.save(project);
        return null;
    }

    private void checkNull(boolean aNull, String s) {
        if (aNull) {
            throw new ValidationException(s);
        }
    }
}
