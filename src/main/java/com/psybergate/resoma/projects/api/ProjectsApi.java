package com.psybergate.resoma.projects.api;

import com.psybergate.resoma.projects.entity.Task;

import java.util.UUID;

/**
 * API exposed to communicate with the projects module
 */
public interface ProjectsApi {

    Task getTask(UUID taskId);
}
