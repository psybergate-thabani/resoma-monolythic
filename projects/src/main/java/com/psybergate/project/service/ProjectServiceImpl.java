package com.psybergate.project.service;

import com.psybergate.project.entity.Project;
import com.psybergate.project.entity.Task;
import com.psybergate.project.repository.ProjectRepository;
import com.psybergate.project.repository.TaskRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

    private TaskRepository taskRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public Project captureProject(Project newProject) {
        validateProject(newProject);
        return projectRepository.save(newProject);
    }

    @Override
    public Project retrieveProject(UUID id) {
        return projectRepository.findByIdAndDeleted(id, false);
    }

    @Override
    @Transactional
    public Project retrieveProject(String code) {
        return projectRepository.findByCodeAndDeleted(code, false);
    }

    @Override
    @Transactional
    public List<Project> retrieveProjects() {
        return projectRepository.findAllByDeleted(false);
    }

    @Override
    @Transactional
    public Project updateProject(Project project) {
        validateProject(project);
        Project updatedProject = projectRepository.save(project);
        return updatedProject;
    }

    @Override
    @Transactional
    public void addPersonToProject(String employeeCode, UUID projectId) {
        Project project = retrieveProject(projectId);
        validateProjectTeamMember(project, employeeCode);
        project.getTeam().add(employeeCode);
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
    public List<Project> retrieveProjects(List<String> projectCodes, boolean deleted) {
        return projectRepository.findAllByCodeInAndDeleted(projectCodes, deleted);
    }

    @Override
    @Transactional
    public void removeDeletedEmployeeFromProjects(String employeeCode) {
        List<Project> projects = retrieveProjects();
        projects.forEach(project -> {
            Set<String> projectTeam = project.getTeam();
            projectTeam.remove(employeeCode);
        });
        projectRepository.saveAll(projects);
    }

    @Override
    public Task retrieveTaskByCode(String code, boolean deleted) {
        return taskRepository.findByCodeAndDeleted(code, deleted);
    }

    @Override
    @Transactional
    public void deleteTask(Task task) {
        validateTask(task);
        task.setDeleted(true);
        Task updatedTask = taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task addTaskToProject(Task newTask, UUID projectId) {
        validateTask(newTask);
        Project project = retrieveProject(projectId);
        validateProjectTask(project, newTask.getCode());
        newTask.setProject(project);
        return taskRepository.save(newTask);
    }

    @Override
    @Transactional
    public List<Task> retrieveTasks(Project project, boolean deleted) {
        return taskRepository.findAllByProjectAndDeleted(project, deleted);
    }

    @Override
    @Transactional
    public void deleteTaskByProject(Project project) {
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
    public Set<Task> retrieveTasksByCodes(Set<String> taskCodes) {
        return taskRepository.findAllByCodeInAndDeleted(taskCodes, false);
    }

    private void validateTask(Task task) {
        List<String> errorMessages = new ArrayList<>();
        if (StringUtils.isBlank(task.getCode())) {
            errorMessages.add("Task code cannot be empty or null");
        }
        if (StringUtils.isBlank(task.getName())) {
            errorMessages.add("Task name cannot be empty or null");
        }
        if (errorMessages.size() > 0) {
            throw new ValidationException(errorMessages.toString());
        }
    }

    private void validateProjectTask(Project project, String taskCode) {
        List<String> errorMessages = new ArrayList<>();
        List<Task> taskList = taskRepository.findAllByProjectAndDeleted(project, false);
        taskList.forEach(task -> {
            if (taskCode.equals(task.getCode()))
                errorMessages.add("Task code " + taskCode + " already exists in project");
        });
        if (errorMessages.size() > 0) {
            throw new ValidationException(errorMessages.toString());
        }
    }

    private void validateProject(Project project) {
        List<String> errorMessages = new ArrayList<>();
        if (StringUtils.isBlank(project.getCode())) {
            errorMessages.add("Project code cannot be empty or null");
        }
        if (StringUtils.isBlank(project.getName())) {
            errorMessages.add("Project name cannot be empty or null");
        }
        if (StringUtils.isBlank(project.getClientCode())) {
            errorMessages.add("Client code cannot be empty or null");
        }
        if (project.getStartDate() == null) {
            errorMessages.add("Start date cannot be empty or null");
        }
        if (project.getType() == null) {
            errorMessages.add("Project type cannot be empty or null");
        }
        if (errorMessages.size() > 0) {
            throw new ValidationException(errorMessages.toString());
        }
    }

    private void validateProjectTeamMember(Project project, String member) {
        List<String> errorMessages = new ArrayList<>();
        if (project.getTeam().contains(member)) {
            errorMessages.add("Employee already added in this project");
        }
        if (errorMessages.size() > 0) {
            throw new ValidationException(errorMessages.toString());
        }
    }

}
