package com.psybergate.resoma.projects.api.impl;

import com.psybergate.resoma.projects.api.ProjectsApi;
import com.psybergate.resoma.projects.entity.Task;
import com.psybergate.resoma.projects.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProjectsApiImpl implements ProjectsApi {

    private ProjectService projectService;

    @Autowired
    public ProjectsApiImpl(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public Task getTask(UUID taskId) {
        return projectService.retrieveTask(taskId);
    }
}
