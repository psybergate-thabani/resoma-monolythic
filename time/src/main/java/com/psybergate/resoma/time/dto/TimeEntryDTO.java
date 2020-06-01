package com.psybergate.resoma.time.dto;

import com.psybergate.resoma.time.entity.TimeEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeEntryDTO {

    @NotNull(message = "{timeentry.notnull}")
    private TimeEntry timeEntry;

    @NotNull(message = "{employeeid.notblank}")
    private UUID employeeId;

    @NotNull(message = "{taskid.notnull}")
    private UUID taskId;
}
