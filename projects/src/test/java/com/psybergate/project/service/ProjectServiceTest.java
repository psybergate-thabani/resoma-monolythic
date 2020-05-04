package com.psybergate.project.service;

import com.psybergate.project.entity.Project;
import com.psybergate.project.entity.ProjectType;
import com.psybergate.project.entity.Task;
import com.psybergate.project.repository.ProjectRepository;
import com.psybergate.project.repository.TaskRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository mockProjectRepository;

    @Mock
    private TaskRepository mockTaskRepository;

    private Project testProject;

    private ProjectService projectService;

    List<Task> tasks = new ArrayList<>();

    @BeforeEach
    public void init() {
        // Arrange
        Set<String> team = new HashSet<>(2);
        team.add("Person1");
        team.add("Person2");
        tasks.add(new Task("tas1", "task1", testProject, false));
        tasks.add(new Task("tas2", "task2", testProject, false));
        testProject = new Project("projCode", "project", "cliCode", LocalDate.now(), LocalDate.now(), team, ProjectType.NON_BILLABLE);
        testProject.generate();
        mockProjectRepository = mock(ProjectRepository.class);
        mockTaskRepository = mock(TaskRepository.class);
        projectService = new ProjectServiceImpl(mockProjectRepository, mockTaskRepository);
    }

    @Test
    public void shouldStoreAProject_whenAProjectIsCaptured() {
        // Arrange
        when(mockProjectRepository.save(testProject)).thenReturn(testProject);

        // Act
        Project savedProject = projectService.captureProject(testProject);

        // Assert
        assertEquals(testProject, savedProject);
        verify(mockProjectRepository, times(1)).save(testProject);
    }

    @Test
    public void shouldReturnAProject_whenProjectIsRetrieved() {
        //Arrange
        when(mockProjectRepository.findByIdAndDeleted(testProject.getId(), false)).thenReturn(testProject);

        // Act
        Project retrievedProject = projectService.retrieveProject(testProject.getId());

        // Assert
        assertEquals(testProject, retrievedProject);
        verify(mockProjectRepository, times(1)).findByIdAndDeleted(testProject.getId(), false);
    }

    @Test
    public void shouldReturnAProject_whenProjectIsRetrievedByProjectCode() {
        //Arrange
        when(mockProjectRepository.findByCodeAndDeleted(testProject.getCode(), false)).thenReturn(testProject);

        // Act
        Project retrievedProject = projectService.retrieveProject(testProject.getCode());

        // Assert
        assertEquals(testProject, retrievedProject);
        verify(mockProjectRepository, times(1)).findByCodeAndDeleted(testProject.getCode(), false);
    }

    @Test
    public void shouldReturnAListOfAllProjectsThatAreNotDeleted_whenAllProjectsAreRetrieved() {
        // Arrange
        List<Project> projects = Arrays.asList(testProject, testProject);
        when(mockProjectRepository.findAllByDeleted(false)).thenReturn(projects);

        // Act
        List<Project> retrievedProjects = projectService.retrieveProjects();

        // Assert
        assertNotNull(retrievedProjects);
        retrievedProjects.forEach(project -> assertFalse(project.isDeleted()));
        projects.forEach(project -> assertEquals(testProject, project));
        verify(mockProjectRepository, times(1)).findAllByDeleted(false);
    }

    @Test
    public void shouldThrowException_whenProjectRequiredFieldsAreIncorrect() {
        // Arrange
        Project newProject = new Project(" ", " ", " ", null, LocalDate.now(), null, null);

        // Act and Assert
        assertThrows(ValidationException.class, () -> projectService.captureProject(newProject));
    }

    @Test
    public void shouldUpdateProject_whenProjectIsBeingUpdated() {
        //Arrange
        when(mockProjectRepository.save(testProject)).thenReturn(testProject);
        // Act
        Project updatedProject = projectService.updateProject(testProject);

        // Assert
        assertNotNull(updatedProject);
        assertEquals(testProject, updatedProject);
        verify(mockProjectRepository, times(1)).save(testProject);
    }

    @Test
    public void shouldAddPersonToProjectTeam_whenPersonIsAddedToProject() {
        // Arrange
        String employeeCode = "emp1";
        UUID projectId = testProject.getId();
        when(mockProjectRepository.findByIdAndDeleted(testProject.getId(), false)).thenReturn(testProject);
        when(mockProjectRepository.save(testProject)).thenReturn(testProject);

        // Act
        projectService.addPersonToProject(employeeCode, projectId);

        // Assert
        verify(mockProjectRepository, times(1)).findByIdAndDeleted(projectId, false);
        verify(mockProjectRepository, times(1)).save(testProject);
        Assertions.assertThat(testProject.getTeam()).contains(employeeCode);
    }

    @Test
    public void shouldDeleteProject_whenProjectIsBeingDeleted() {
        //Arrange
        when(mockProjectRepository.findByIdAndDeleted(testProject.getId(), false)).thenReturn(testProject);

        // Act
        projectService.deleteProject(testProject.getId());

        // Assert
        verify(mockProjectRepository, times(1)).save(testProject);
    }

    @Test
    public void shouldReturnListOfProjects_WhenProjectCodesAreSupplied() {
        //Arrange
        Set<String> teamList1 = new HashSet<>();
        teamList1.add("emp1");
        teamList1.add("emp2");
        Set<String> teamList2 = new HashSet<>();
        teamList2.add("emp3");
        teamList2.add("emp4");
        Project localTestProject1 = new Project("projCode1", "project 1", "cliCode1",
                LocalDate.now(), LocalDate.now(), teamList1, ProjectType.NON_BILLABLE);
        localTestProject1.setDeleted(true);
        Project localTestProject2 = new Project("projCode2", "project 2", "cliCode2",
                LocalDate.now(), LocalDate.now(), teamList2, ProjectType.NON_BILLABLE);
        List<Project> projectList = Arrays.asList(testProject, localTestProject1, localTestProject2);
        List<String> projectCodes = Arrays.asList("projCode", "projCode1", "projCode2");
        when(mockProjectRepository.findAllByCodeInAndDeleted(projectCodes, false)).thenReturn(projectList);

        //Act
        List<Project> retrievedProjects = projectService.retrieveProjects(projectCodes, false);

        //Verify
        assertNotNull(retrievedProjects);
        assertEquals(retrievedProjects, projectList);
        verify(mockProjectRepository, times(1)).findAllByCodeInAndDeleted(projectCodes, false);
    }

    @Test
    public void shouldRetrieveTasks_whenGivenProject() {
        //Arrange
        when(mockTaskRepository.findAllByProjectAndDeleted(testProject, false)).thenReturn(tasks);

        //Act
        List<Task> tasksRetrieved = projectService.retrieveTasks(testProject, false);

        //Assert
        assertNotNull(tasksRetrieved);
        assertEquals(tasksRetrieved, tasks);
        verify(mockTaskRepository, times(1)).findAllByProjectAndDeleted(testProject, false);
    }

    @Test
    public void shouldRemoveEmployeeFromProjects_whenEmployeeIsDeleted() {
        // Arrange
        String employeeCode = "person1";
        List<Project> testProjects = Arrays.asList(testProject);
        when(mockProjectRepository.findAllByDeleted(false)).thenReturn(testProjects);
        when(mockProjectRepository.saveAll(testProjects)).thenReturn(testProjects);

        // Act
        projectService.removeDeletedEmployeeFromProjects(employeeCode);

        // Assert
        verify(mockProjectRepository, times(1)).findAllByDeleted(false);
        verify(mockProjectRepository, times(1)).saveAll(testProjects);
    }

    @Test
    public void returnTask_whenRetrieveTaskByCode() {
        // Arrange
        String code = "task1";
        Task task = new Task(code, "analysis", testProject, false);
        when(mockTaskRepository.findByCodeAndDeleted(any(String.class), any(Boolean.class))).thenReturn(task);

        // Act
        Task returnedTask = projectService.retrieveTaskByCode(code, false);

        // Assert
        assertNotNull(returnedTask);
        assertEquals(code, returnedTask.getCode());
    }

    @Test
    public void shouldAddTaskToProject_whenTaskIsCreatedForAProject() {
        // Arrange
        Task newTask = new Task("tas3", "task3", testProject, false);

        // Act
        projectService.addTaskToProject(newTask, testProject.getId());

        // Assert
        verify(mockTaskRepository, times(1)).save(newTask);
    }

    @Test
    public void shouldThrowValidationException_whenRequiredFieldsOfTaskAreIncorrect() {
        // Arrange
        Task newTask = new Task("", "", null, false);

        // Act and Assert
        assertThrows(ValidationException.class, () -> projectService.addTaskToProject(newTask, UUID.randomUUID()));
    }

    @Test
    public void shouldDeletedTask_whenTaskIsDeleted() {
        // Arrange
        Task task = new Task("task1", "analysis", testProject, false);
        when(mockTaskRepository.save(task)).thenReturn(any());

        // Act
        projectService.deleteTask(task);

        // Assert
        assertTrue(task.isDeleted());
        verify(mockTaskRepository, times(1)).save(task);
    }

    @Test
    public void shouldDeleteTaskById_wheTaskIsDeleted(){
        // Arrange
        Task task = new Task("task1", "analysis", testProject, false);
        when(mockTaskRepository.getOne(task.getId())).thenReturn(task);
        when(mockTaskRepository.save(task)).thenReturn(task);

        // Act
        projectService.deleteTask(task.getId());

        // Assert
        assertTrue(task.isDeleted());
        verify(mockTaskRepository, times(1)).save(task);
    }

    @Test
    public void shouldTasks_whenRetrieveTasksByCodes() {
        // Arrange
        Set<Task> tasks = Arrays.asList(new Task("task1", "analysis", testProject, false))
                .stream().collect(Collectors.toSet());
        when(mockTaskRepository.findAllByCodeInAndDeleted(any(Set.class), any(Boolean.class))).thenReturn(tasks);

        // Act
        Set<String> taskCodes = Arrays.asList("task1").stream().collect(Collectors.toSet());
        Set<Task> retrievedTasks = projectService.retrieveTasksByCodes(taskCodes);

        // Assert
        assertNotNull(retrievedTasks);
        verify(mockTaskRepository, times(1)).findAllByCodeInAndDeleted(taskCodes, false);
    }
}
