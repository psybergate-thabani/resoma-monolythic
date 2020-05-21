package com.psybergate.resoma.project.dto;

import com.psybergate.resoma.project.entity.Task;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class TaskDTO {

    private Task task;

    private UUID projectId;
}
