package com.psybergate.resoma.project.service;


import com.psybergate.resoma.people.entity.Employee;
import com.psybergate.resoma.project.entity.Allocation;
import com.psybergate.resoma.project.entity.Project;
import com.psybergate.resoma.project.entity.ProjectType;
import com.psybergate.resoma.project.entity.Task;
import com.psybergate.resoma.project.repository.ProjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    private ProjectService projectService;
    private Project project;
    private Set<Employee> team = new HashSet<>();

    @BeforeEach
    void setUp() {
        projectService = new ProjectServiceImpl(projectRepository);
        team.add(new Employee("emp1", "John", "Doe", "JohnD@resoma.com", "78 Home Address, Johannesburg",
                "79 Postal Address, Johannesburg", LocalDateTime.now(), LocalDate.now(), "Developer", "Active"));
        project = new Project("proj1", "First Project", "client1", LocalDate.now(), null, ProjectType.BILLABLE);
    }

    @Test
    void shouldCreateProject_whenProjectIsCaptured() {
        //Arrange
        when(projectRepository.save(project)).thenReturn(project);

        //Act
        Project resultProject = projectService.captureProject(project);

        //Assert
        assertNotNull(resultProject);
        assertEquals(project, resultProject);
    }

    @Test
    void shouldReturnProject_whenProjectIsRetrieved() {
        //Arrange
        UUID id = project.getId();
        when(projectRepository.findByIdAndDeleted(id, false)).thenReturn(project);

        //Act
        Project resultProject = projectService.retrieveProject(id, false);

        //Assert
        assertNotNull(resultProject);
        assertEquals(project, resultProject);
    }

    @Test
    void shouldReturnListOfProjects_whenProjectsAreRetrieved() {
        //Arrange
        Project projectA = new Project("projA", "First Project", "client1", LocalDate.now(), null, ProjectType.BILLABLE);
        Project projectB = new Project("projB", "Second Project", "client1", LocalDate.now(), null, ProjectType.BILLABLE);
        Project projectC = new Project("projC", "Third Project", "client1", LocalDate.now(), null, ProjectType.BILLABLE);
        List<Project> projects = Arrays.asList(projectA, projectB, projectC);
        when(projectRepository.findAllByDeleted(false)).thenReturn(projects);

        //Act
        List<Project> resultProjects = projectService.retrieveProjects(false);

        //Assert
        assertEquals(3, resultProjects.size());
        assertEquals(projects, resultProjects);
        resultProjects.forEach(p -> assertTrue(resultProjects.contains(p)));
        verify(projectRepository, times(1)).findAllByDeleted(false);
    }

    @Test
    void shouldUpdateProject_whenProjectIsUpdated() {
        //Arrange
        when(projectRepository.getOne(project.getId())).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        //Act
        Project resultProject = projectService.updateProject(project);

        //Assert
        assertNotNull(resultProject);
        assertEquals(project, resultProject);

    }

    @Test
    void shouldDeleteProject_whenProjectIsDeleted() {
        //Arrange
        UUID id = project.getId();
        Task task = new Task("task1", "First Task", false);
        Project project = new Project();
        project.getTasks().add(task);
        when(projectRepository.findByIdAndDeleted(id, false)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);

        //Act
        projectService.deleteProject(id);

        //Assert
        verify(projectRepository, times(1)).findByIdAndDeleted(id, false);
        verify(projectRepository, times(1)).save(project);
        assertTrue(project.isDeleted());
        assertTrue(task.isDeleted());
    }

    @Test
    void shouldThrowValidationException_whenProjectIsDeletedAndProjectDoesNotExist() {
        //Arrange
        UUID id = project.getId();
        when(projectRepository.findByIdAndDeleted(id, false)).thenReturn(null);

        //Act
        Assertions.assertThrows(ValidationException.class, () -> {
            projectService.deleteProject(id);
        });
    }

    @Test
    void shouldAddTaskToProject_whenTaskIsAddedToProject() {
        //Arrange
        Task task = new Task("task1", "First Task", false);
        UUID id = project.getId();

        when(projectRepository.findByIdAndDeleted(id, false)).thenReturn(project);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        //Act
        Task resultTask = projectService.addTaskToProject(task, id);

        //Assert
        assertNotNull(resultTask);
        assertEquals(task, resultTask);
        verify(projectRepository, times(1)).findByIdAndDeleted(id, false);
        verify(projectRepository, times(1)).save(any(Project.class));

    }

    @Test
    void shouldThrowValidationException_whenTaskIsAddedToProjectThatDoesNotExist() {
        //Arrange
        Task task = new Task("task1", "First Task", false);
        UUID id = project.getId();
        when(projectRepository.findByIdAndDeleted(id, false)).thenReturn(null);

        //Act And Assert
        Assertions.assertThrows(ValidationException.class, () -> {
            Task resultTask = projectService.addTaskToProject(task, id);
        });
    }

    @Test
    void shouldRetrieveTasks_whenTasksAreRetrieved() {
        //Arrange
        Task task = new Task("task1", "First Task", false);
        Project project = new Project();
        project.getTasks().add(task);
        when(projectRepository.findByIdAndDeleted(project.getId(),false)).thenReturn(project);

        //Act
        Set<Task> resultTasks = projectService.retrieveTasks(project.getId(), false);

        //Assert
        assertNotNull(resultTasks);
        assertEquals(1, resultTasks.size());
    }

    @Test
    void shouldDeleteTask_whenTaskIsDeleted() {
        //Arrange
        Task task = new Task("task1", "First Task", false);
        task.setId(UUID.randomUUID());
        UUID id = task.getId();
        Project project = new Project();
        project.getTasks().add(task);
        when(projectRepository.findFirstByTasks_id(id)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);

        //Act
        projectService.deleteTask(id);

        //Assert
        verify(projectRepository, times(1)).findFirstByTasks_id(id);
        verify(projectRepository, times(1)).save(project);
        assertTrue(task.isDeleted());
    }

    @Test
    void shouldReturnTask_whenTaskIsRetrievedById() {
        //Arrange
        Task task = new Task("task1", "First Task", false);
        Project project = new Project();
        project.getTasks().add(task);
        UUID taskId = task.getId();
        when(projectRepository.findFirstByTasks_id(taskId)).thenReturn(project);

        //Act
        Task resultTask = projectService.retrieveTask(taskId);

        //Assert
        assertNotNull(resultTask);
        assertEquals(task, resultTask);
        verify(projectRepository, times(1)).findFirstByTasks_id(taskId);
    }

    @Test
    void shouldReturnAllocation_whenAllocationIsRetrievedById() {
        //Arrange
        Allocation allocation = new Allocation(new Employee());
        allocation.setId(UUID.randomUUID());
        UUID allocationId = allocation.getId();
        Project project = new Project();
        project.getAllocations().add(allocation);
        when(projectRepository.findFirstByAllocationsId(allocationId)).thenReturn(project);

        //Act
        Allocation resultAllocation = projectService.retrieveAllocation(allocationId);

        //Assert
        assertNotNull(resultAllocation);
        assertEquals(allocation, resultAllocation);
        verify(projectRepository, times(1)).findFirstByAllocationsId(allocationId);
    }

    @Test
    void shouldDeallocate_whenEmployeeIsDeallocated() {
        //Arrange
        Allocation allocation = new Allocation();
        allocation.setId(UUID.randomUUID());
        Project project = new Project();
        project.getAllocations().add(allocation);
        when(projectRepository.findFirstByAllocationsId(allocation.getId())).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);

        //Act
        projectService.deallocateEmployee(allocation.getId());

        //Assert
        verify(projectRepository, times(1)).findFirstByAllocationsId(allocation.getId());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void shouldRetrieveAllocations_whenAllocationsAreRetrievedByProject() {
        //Arrange
        Allocation allocation = new Allocation();
        Project project = new Project();
        project.getAllocations().add(allocation);
        when(projectRepository.getOne(project.getId()))
                .thenReturn(project);

        //Act
        Set<Allocation> resultAllocations = projectService.retrieveAllocations(project.getId());

        //Assert
        assertNotNull(resultAllocations);
        assertEquals(1, resultAllocations.size());
    }

    @Test
    void shouldAllocateEmployeeToProject_whenEmployeeIsAllocatedToProject() {
        //Arrange
        Allocation allocation = new Allocation();
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setEmployeeCode("emp12");
        allocation.setId(UUID.randomUUID());
        allocation.setEmployee(employee);
        Project project = new Project();
        project.getAllocations().add(allocation);
        project.setId(UUID.randomUUID());
        when(projectRepository.getOne(project.getId())).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);

        //Act
        Allocation resultAllocation = projectService.allocateEmployee(project.getId(), allocation);

        //Assert
        assertNotNull(resultAllocation);
        assertEquals(allocation, resultAllocation);
        verify(projectRepository, times(1)).getOne(project.getId());
        verify(projectRepository, times(1)).save(project);

    }

    @Test
    void shouldRetrieveAllocations_whenAllocationsAreRetrievedByProjectId() {
        //Arrange
        Allocation allocation = new Allocation();
        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.getAllocations().add(allocation);
        when(projectRepository.getOne(project.getId()))
                .thenReturn(project);

        //Act
        Set<Allocation> resultAllocations = projectService.retrieveAllocations(project.getId());

        //Assert
        assertNotNull(resultAllocations);
        assertEquals(1, resultAllocations.size());
        verify(projectRepository, times(1)).getOne(project.getId());
    }
}
