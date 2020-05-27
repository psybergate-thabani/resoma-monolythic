package com.psybergate.resoma.gateway.controller;

import com.psybergate.resoma.gateway.dto.AllocationDTO;
import com.psybergate.resoma.gateway.dto.TaskDTO;
import com.psybergate.resoma.people.entity.Employee;
import com.psybergate.resoma.people.service.EmployeeService;
import com.psybergate.resoma.project.entity.Allocation;
import com.psybergate.resoma.project.entity.Project;
import com.psybergate.resoma.project.entity.Task;
import com.psybergate.resoma.project.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/project")
public class ProjectController {

    private ProjectService projectService;
    private EmployeeService employeeService;

    public ProjectController(ProjectService projectService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    @PostMapping("v1/project-entries")
    public ResponseEntity<Project> captureProject(@RequestBody @Valid Project project) {
        Project savedProject = projectService.captureProject(project);
        return ResponseEntity.ok(savedProject);
    }

    @GetMapping(value = "v1/project-entries/{projectId}", params = {"deleted"})
    public ResponseEntity<Project> retrieveProject(@PathVariable UUID projectId, @RequestParam("deleted") boolean deleted) {
        return ResponseEntity.ok(projectService.retrieveProject(projectId, deleted));
    }

    @GetMapping(value = "v1/project-entries", params = {"deleted"})
    public ResponseEntity<List<Project>> retrieveProjects(@RequestParam("deleted") Boolean deleted) {
        return ResponseEntity.ok(projectService.retrieveProjects(deleted));
    }

    @PutMapping("v1/project-entries/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable UUID projectId, @RequestBody @Valid Project project) {
        if (!projectId.equals(project.getId()))
            throw new ValidationException("Id in request body does not match Id in url path");
        return ResponseEntity.ok(projectService.updateProject(project));
    }

    @PutMapping(value = "v1/project-entries/{projectId}/tasks")
    public ResponseEntity<Task> addTaskToTheProject(@RequestBody TaskDTO newTask,
                                                    @PathVariable UUID projectId) {

        if (!projectId.equals(newTask.getProjectId()))
            throw new ValidationException("Project id and projectId in taskDTO does not match.");

        return ResponseEntity.ok(projectService.addTaskToProject(newTask.getTask(), projectId));
    }

    @GetMapping(value = "v1/project-entries/{projectId}/tasks", params = {"deleted"})
    public ResponseEntity<List<Task>> retrieveTasksByProjectId(@PathVariable UUID projectId,
                                                               @RequestParam("deleted") Boolean deleted) {
        Project project = projectService.retrieveProject(projectId, deleted);
        return ResponseEntity.ok(projectService.retrieveTasks(project));
    }

    @DeleteMapping("v1/project-entries/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("v1/project-entries/{projectId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID taskId) {
        projectService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("v1/project-entries/{projectId}/allocations")
    public ResponseEntity<Set<Allocation>> retrieveProjectAllocations(@PathVariable UUID projectId) {
        Project project = projectService.retrieveProject(projectId, false);
        return ResponseEntity.ok(projectService.retrieveAllocations(project));
    }

    @GetMapping(value = "v1/project-entries/{projectId}/allocations", params = "deleted")
    public ResponseEntity<Set<Allocation>> retrieveProjectAllocations(@PathVariable UUID projectId, Boolean deleted) {
        Project project = projectService.retrieveProject(projectId, deleted);
        return ResponseEntity.ok(projectService.retrieveAllocations(project, deleted));
    }

    @GetMapping("v1/project-entries/{projectId}/allocations/{allocationId}")
    public ResponseEntity<Allocation> retrieveAllocation(@PathVariable UUID allocationId) {
        return ResponseEntity.ok(projectService.retrieveAllocation(allocationId));
    }

    @PostMapping("v1/project-entries/{projectId}/allocations")
    public ResponseEntity<Allocation> allocateEmployee(@PathVariable UUID projectId, @RequestBody AllocationDTO allocationDTO) {
        if(!projectId.equals(allocationDTO.getProjectId()))
            throw new ValidationException("Project id and projectId in AllocationDTO does not match.");

        Allocation allocation = buildAllocation(allocationDTO);
        return ResponseEntity.ok(projectService.allocateEmployee(allocation));
    }

    @DeleteMapping("v1/project-entries/{projectId}/allocations/{allocationId}")
    public ResponseEntity<Void> deallocateEmployee(@PathVariable UUID allocationId) {
        projectService.deallocateEmployee(allocationId);
        return ResponseEntity.ok().build();
    }

    private Allocation buildAllocation(AllocationDTO allocationDTO) {
        Employee employee = employeeService.retrieveEmployee(allocationDTO.getEmployeeId());
        Project project = projectService.retrieveProject(allocationDTO.getProjectId(), false);
        if (Objects.isNull(employee))
            throw new ValidationException("Employee with id \"" + allocationDTO.getEmployeeId() + "\" does not exist");
        if (Objects.isNull(project))
            throw new ValidationException("Project with id \"" + allocationDTO.getProjectId() + "\" does not exist");
        return new Allocation(project, employee);
    }

}
