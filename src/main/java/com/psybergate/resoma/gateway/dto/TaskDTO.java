package com.psybergate.resoma.gateway.dto;

import com.psybergate.resoma.projects.entity.Task;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class TaskDTO {

    private Task task;

    private UUID projectId;
}
