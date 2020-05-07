package com.psybergate.resoma.projects.controller;

import com.psybergate.resoma.projects.dto.AllocationDTO;
import com.psybergate.resoma.projects.entity.Project;
import com.psybergate.resoma.projects.entity.Task;
import com.psybergate.resoma.projects.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("v1/project-entries")
    public ResponseEntity<Project> captureProject(@RequestBody Project project) {
        Project savedProject = projectService.captureProject(project);
        return ResponseEntity.ok(savedProject);
    }

    @GetMapping(value = "v1/project-entries/{projectId}")
    public ResponseEntity<Project> retrieveProject(@PathVariable UUID projectId) {
        return ResponseEntity.ok(projectService.retrieveProject(projectId));
    }

    @GetMapping(value = "v1/project-entries", params = {"deleted"})
    public ResponseEntity<List<Project>> retrieveProjects(@RequestParam("deleted") Boolean deleted) {
        return ResponseEntity.ok(projectService.retrieveProjects());
    }

    @PutMapping("v1/project-entries/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable UUID projectId, @RequestBody Project project) {
        if (!projectId.equals(project.getId()))
            throw new ValidationException("Id in request body does not match Id in url path");
        return ResponseEntity.ok(projectService.updateProject(project));
    }

    @PutMapping(value = "v1/project-entries/{projectId}/tasks")
    public ResponseEntity<Task> addTaskToTheProject(@RequestBody Task newTask,
                                                    @PathVariable UUID projectId) {
        return ResponseEntity.ok(projectService.addTaskToProject(newTask, projectId));
    }

    @GetMapping(value = "v1/project-entries/{projectId}/tasks", params = {"deleted"})
    public ResponseEntity<List<Task>> retrieveTasksByProjectId(@PathVariable UUID projectId,
                                                               @RequestParam("deleted") Boolean deleted) {
        Project project = projectService.retrieveProject(projectId);
        return ResponseEntity.ok(projectService.retrieveTasks(project));
    }

    @PutMapping("v1/project-entries/{projectId}/employee-allocations")
    public ResponseEntity<Project> addPersonToAProject(@RequestBody AllocationDTO allocationDTO, @PathVariable UUID projectId) {
        if (!projectId.equals(allocationDTO.getProjectId()))
            throw new ValidationException("Id in request body does not match Id in url path");
        projectService.addPersonToProject(allocationDTO.getEmployeeCode(), allocationDTO.getProjectId());
        return ResponseEntity.ok(projectService.retrieveProject(projectId));
    }

    @DeleteMapping("v1/project-entries/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("v1/project-entries/{projectId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID taskId, @PathVariable UUID projectId) {
        projectService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }
}
