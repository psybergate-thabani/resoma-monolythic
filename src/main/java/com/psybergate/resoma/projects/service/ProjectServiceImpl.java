package com.psybergate.resoma.projects.service;

import com.psybergate.resoma.projects.entity.Project;
import com.psybergate.resoma.projects.entity.Task;
import com.psybergate.resoma.projects.repository.ProjectRepository;
import com.psybergate.resoma.projects.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

    private TaskRepository taskRepository;

    @Autowired
    public ProjectServiceImpl(
            ProjectRepository projectRepository,
            TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public Project captureProject(Project newProject) {
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
    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    @Transactional
    public void addPersonToProject(String employeeCode, UUID projectId) {
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
    public Task addTaskToProject(Task newTask, UUID projectId) {
        Project project = retrieveProject(projectId);
        newTask.setProject(project);
        return taskRepository.save(newTask);
    }

    @Override
    @Transactional
    public List<Task> retrieveTasks(Project project) {
        return taskRepository.findAllByProjectAndDeleted(project, false);
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

}
