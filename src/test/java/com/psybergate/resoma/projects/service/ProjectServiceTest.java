package com.psybergate.resoma.projects.service;

import com.psybergate.resoma.people.entity.Employee;
import com.psybergate.resoma.projects.entity.Allocation;
import com.psybergate.resoma.projects.entity.Project;
import com.psybergate.resoma.projects.entity.ProjectType;
import com.psybergate.resoma.projects.entity.Task;
import com.psybergate.resoma.projects.repository.AllocationRepository;
import com.psybergate.resoma.projects.repository.ProjectRepository;
import com.psybergate.resoma.projects.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private AllocationRepository allocationRepository;
    private ProjectService projectService;
    private Project project;
    private Set<Employee> team = new HashSet<>();

    @BeforeEach
    void setUp() {
        projectService = new ProjectServiceImpl(projectRepository, taskRepository, allocationRepository);
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
        Project resultProject = projectService.retrieveProject(id);

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
        List<Project> resultProjects = projectService.retrieveProjects();

        //Assert
        assertEquals(3, resultProjects.size());
        assertEquals(projects, resultProjects);
        resultProjects.forEach(p -> assertTrue(resultProjects.contains(p)));
        verify(projectRepository, times(1)).findAllByDeleted(false);
    }

    @Test
    void shouldUpdateProject_whenProjectIsUpdated() {
        //Arrange
        when(projectRepository.save(project)).thenReturn(project);
        //Act
        Project resultProject = projectService.updateProject(project);

        //Assert
        assertNotNull(resultProject);
        assertEquals(project, resultProject);

    }

    @Test
    void shouldAddEmployeeToProject_whenPersonIsAddedToProject() {
        //Arrange
        UUID id = project.getId();
        when(projectRepository.findByIdAndDeleted(id,false)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);

        //Act
        UUID employeeId = UUID.randomUUID();
        projectService.addPersonToProject(employeeId, id);

        //Assert
        verify(projectRepository, times(1)).findByIdAndDeleted(id,false);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void shouldDeleteProject_whenProjectIsDeleted() {
        //Arrange
        UUID id = project.getId();
        when(projectRepository.findByIdAndDeleted(id, false)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        Task task = new Task("task1", "First Task", project, false);
        when(taskRepository.findAllByProjectAndDeleted(project, false))
                .thenReturn(Collections.singletonList(task));

        //Act
        projectService.deleteProject(id);

        //Assert
        verify(projectRepository, times(1)).save(project);
        verify(taskRepository, times(1)).findAllByProjectAndDeleted(project, false);
        verify(taskRepository, times(1)).save(any(Task.class));

    }

    @Test
    void shouldAddTaskToProject_whenTaskIsAddedToProject() {
        //Arrange
        Task task = new Task("task1", "First Task", project, false);
        UUID id = project.getId();
        when(projectRepository.findByIdAndDeleted(id, false)).thenReturn(project);
        when(taskRepository.save(task)).thenReturn(task);

        //Act
        Task resultTask = projectService.addTaskToProject(task, id);

        //Assert
        assertNotNull(resultTask);
        assertEquals(task, resultTask);
        verify(projectRepository, times(1)).findByIdAndDeleted(id, false);
        verify(taskRepository, times(1)).save(task);

    }

    @Test
    void shouldRetrieveTasks_whenTasksAreRetrieved() {
        //Arrange
        Task task = new Task("task1", "First Task", project, false);
        when(taskRepository.findAllByProjectAndDeleted(project, false))
                .thenReturn(Collections.singletonList(task));

        //Act
        List<Task> resultTasks = projectService.retrieveTasks(project);

        //Assert
        assertNotNull(resultTasks);
        assertEquals(1, resultTasks.size());
    }

    @Test
    void shouldDeleteTaskByProject_whenTaskIsDeletedByProject() {
        //Arrange
        Task task = new Task("task1", "First Task", project, false);

        when(taskRepository.findAllByProjectAndDeleted(project, false))
                .thenReturn(Collections.singletonList(task));
        when(taskRepository.save(task)).thenReturn(task);

        //Act
        projectService.deleteTaskByProject(project);

        //Assert
        verify(taskRepository, times(1)).findAllByProjectAndDeleted(project, false);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void shouldDeleteTask_whenTaskIsDeleted() {
        //Arrange
        Task task = new Task("task1", "First Task", project, false);
        UUID id = task.getId();
        when(taskRepository.getOne(id)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);

        //Act
        projectService.deleteTask(id);

        //Assert
        verify(taskRepository, times(1)).getOne(id);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void shouldReturnTask_whenTaskIsRetrievedById() {
        //Arrange
        Task task = new Task("task1", "First Task", project, false);
        UUID taskId = task.getId();
        when(taskRepository.findByIdAndDeleted(taskId,false)).thenReturn(task);

        //Act
        Task resultTask = projectService.retrieveTask(taskId);

        //Assert
        assertNotNull(resultTask);
        assertEquals(task, resultTask);
        verify(taskRepository, times(1)).findByIdAndDeleted(taskId,false);
    }

    @Test
    void shouldReturnAllocation_whenAllocationIsRetrievedById() {
        //Arrange
        Allocation allocation = new Allocation(project, new Employee());
        UUID allocationId = allocation.getId();
        when(allocationRepository.getOne(allocationId)).thenReturn(allocation);

        //Act
        Allocation resultAllocation = projectService.retrieveAllocation(allocationId);

        //Assert
        assertNotNull(resultAllocation);
        assertEquals(allocation, resultAllocation);
        verify(allocationRepository, times(1)).getOne(allocationId);
    }

    @Test
    void shouldDeallocate_whenEmployeeIsDeallocated() {
        //Arrange
        Allocation allocation = new Allocation();
        UUID id = allocation.getId();
        when(allocationRepository.getOne(id)).thenReturn(allocation);
        when(allocationRepository.save(allocation)).thenReturn(allocation);

        //Act
        projectService.deallocateEmployee(id);

        //Assert
        verify(allocationRepository, times(1)).getOne(id);
        verify(allocationRepository, times(1)).save(allocation);
    }

    @Test
    void shouldRetrieveAllocations_whenAllocationsAreRetrievedByProject() {
        //Arrange
        Allocation allocation = new Allocation();
        when(allocationRepository.findAllByProject(project))
                .thenReturn(Collections.singletonList(allocation).stream().collect(Collectors.toSet()));

        //Act
        Set<Allocation> resultAllocations = projectService.retrieveAllocations(project);

        //Assert
        assertNotNull(resultAllocations);
        assertEquals(1, resultAllocations.size());
    }

    @Test
    void shouldAllocateEmployeeToProject_whenEmployeeIsAllocatedToProject() {
        //Arrange
        Allocation allocation = new Allocation();
        when(allocationRepository.save(allocation)).thenReturn(allocation);

        //Act
        Allocation resultAllocation = projectService.allocateEmployee(allocation);

        //Assert
        assertNotNull(resultAllocation);
        assertEquals(allocation, resultAllocation);
        verify(allocationRepository, times(1)).save(allocation);

    }

    @Test
    void shouldRetrieveAllocations_whenAllocationsAreRetrievedByProjectAndDeleted() {
        //Arrange
        Allocation allocation = new Allocation();
        when(allocationRepository.findAllByProjectAndDeleted(any(Project.class), anyBoolean()))
                .thenReturn(Collections.singletonList(allocation).stream().collect(Collectors.toSet()));

        //Act
        Set<Allocation> resultAllocations = projectService.retrieveAllocations(project, false);

        //Assert
        assertNotNull(resultAllocations);
        assertEquals(1, resultAllocations.size());
    }


}
