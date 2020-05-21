package com.psybergate.resoma.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AllocationDTO {

    private UUID projectId;

    private UUID employeeId;
}
