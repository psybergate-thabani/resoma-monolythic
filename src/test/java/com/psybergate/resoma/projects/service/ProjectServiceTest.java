package com.psybergate.resoma.projects.service;

import com.psybergate.resoma.people.entity.Employee;
import com.psybergate.resoma.projects.entity.Project;
import com.psybergate.resoma.projects.entity.ProjectType;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TaskRepository taskRepository;
    private ProjectService projectService;
    private Project project;
    private Set<Employee> team = new HashSet<>();

    @BeforeEach
    void setUp() {
        projectService = new ProjectServiceImpl(projectRepository, taskRepository);
        team.add(new Employee("emp1", "John", "Doe", "JohnD@resoma.com", "78 Home Address, Johannesburg",
                "79 Postal Address, Johannesburg", LocalDateTime.now(), LocalDate.now(), "Developer", "Active"));
        project = new Project("proj1", "First Project", "client1", LocalDate.now(), null, team, ProjectType.BILLABLE);
        project.generate();
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
        Project projectA = new Project("projA", "First Project", "client1", LocalDate.now(), null, team, ProjectType.BILLABLE);
        Project projectB = new Project("projB", "Second Project", "client1", LocalDate.now(), null, team, ProjectType.BILLABLE);
        Project projectC = new Project("projC", "Third Project", "client1", LocalDate.now(), null, team, ProjectType.BILLABLE);
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
    void addPersonToProject() {
    }

    @Test
    void deleteProject() {
    }

    @Test
    void addTaskToProject() {
    }

    @Test
    void retrieveTasks() {
    }

    @Test
    void deleteTaskByProject() {
    }

    @Test
    void deleteTask() {
    }
}