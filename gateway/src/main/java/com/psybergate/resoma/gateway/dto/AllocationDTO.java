package com.psybergate.resoma.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AllocationDTO {

    @NotNull(message = "{projectid.notnull}")
    private UUID projectId;

    @NotNull(message = "{employeeid.notblank}")
    private UUID employeeId;
}
