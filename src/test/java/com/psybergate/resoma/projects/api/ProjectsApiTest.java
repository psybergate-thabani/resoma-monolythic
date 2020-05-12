package com.psybergate.resoma.projects.api;

import com.psybergate.resoma.projects.api.impl.ProjectsApiImpl;
import com.psybergate.resoma.projects.entity.Project;
import com.psybergate.resoma.projects.entity.ProjectType;
import com.psybergate.resoma.projects.entity.Task;
import com.psybergate.resoma.projects.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectsApiTest {

    @Mock
    private ProjectService projectService;

    private ProjectsApi projectsApi;

    @BeforeEach
    void setUp() {
        projectsApi = new ProjectsApiImpl(projectService);
    }

    @Test
    void shouldReturnTask_whenTaskIsRetrievedWithId() {
        //Arrange
        Project project = new Project("proj1", "First Project", "client1", LocalDate.now(), null, ProjectType.BILLABLE);
        Task task = new Task("task1", "First Task", project, false);
        UUID taskId = task.getId();
        when(projectService.retrieveTask(taskId)).thenReturn(task);

        //Act
        Task resultTask = projectsApi.getTask(taskId);

        //Assert
        assertNotNull(resultTask);
        assertNotNull(taskId);
        assertEquals(task,resultTask);
        verify(projectService,times(1)).retrieveTask(taskId);

    }

}