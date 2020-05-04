package com.psybergate.project.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AllocationDTO {

    public String employeeCode;

    public UUID projectId;

}
