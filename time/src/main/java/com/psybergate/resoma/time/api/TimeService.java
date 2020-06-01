package com.psybergate.resoma.time.api;

import com.psybergate.resoma.time.dto.TimeEntryDTO;
import com.psybergate.resoma.time.entity.TimeEntry;

import java.util.List;
import java.util.UUID;

public interface TimeService{

    TimeEntry captureTime(TimeEntry timeEntry);

    List<TimeEntry> retrieveEntries(Boolean deleted);

    TimeEntry retrieveEntry(UUID entryId);

    TimeEntry updateEntry(TimeEntry timeEntry);

    void deleteEntry(UUID entryId);

    TimeEntry submitEntry(TimeEntry timeEntry);

    TimeEntry approveEntry(TimeEntry timeEntry);

    TimeEntry rejectEntry(TimeEntry timeEntry);

    List<TimeEntry> approveEntries(List<TimeEntry> entries);

    List<TimeEntry> submitEntries(List<TimeEntry> entries);

    List<TimeEntry> rejectEntries(List<TimeEntry> entries);

    TimeEntry captureTime2(TimeEntryDTO timeEntryDTO);
}