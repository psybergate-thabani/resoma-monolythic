package com.psybergate.resoma.time.dto;

import com.psybergate.resoma.time.entity.TimeEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeEntryDTO {

    private TimeEntry timeEntry;

    private UUID employeeId;

    private UUID taskId;
}
